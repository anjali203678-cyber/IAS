package com.example.data.firebase

import android.util.Log
import com.example.data.model.BtsMember
import com.example.data.model.MotivationQuote
import com.example.data.model.QuoteLabelType
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

class AiTeacherRepository {

    companion object {
        private const val TAG = "AiTeacherRepository"
        private const val MODEL_NAME = "gemini-3.8-flash"

        private val UPSC_TEACHER_SYSTEM_INSTRUCTION = """
            You are the personal UPSC Civil Services (IAS 2029) coach for a beginner student who completed Class 12 in the Science stream.
            Student Preferences:
            - Optional Subject: History.
            - Primary teaching language: Simple, clear, warm Hindi (Devanagari), gradually introducing important English UPSC terminology with Hindi meanings in parentheses.
            - Always start from absolute basics. Never assume prior Humanities or UPSC knowledge.
            - Learning need: Provide memory tricks, visual analogies, timelines, and active recall questions because memorizing facts is hard.
            - Strict Honesty Rule: NEVER invent fake facts, fake citations, fake UPSC Previous Year Questions (PYQs), or unverified statistics. If unsure of a specific year or quote source, clearly state that it should be verified against official NCERT/UPSC archives.
        """.trimIndent()
    }

    private val chatModel by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            systemInstruction = content { text(UPSC_TEACHER_SYSTEM_INSTRUCTION) }
        )
    }

    private val quoteBatchSchema = Schema.obj(
        properties = mapOf(
            "quotes" to Schema.array(
                items = Schema.obj(
                    properties = mapOf(
                        "member" to Schema.enumeration(
                            values = listOf("RM", "JIN", "SUGA", "JHOPE", "JIMIN", "V", "JUNGKOOK"),
                            description = "BTS member theme"
                        ),
                        "contextTag" to Schema.enumeration(
                            values = listOf("MORNING", "SESSION", "BREAK", "CHAPTER_COMPLETE", "TEST_RESULT", "GOAL_COMPLETE", "EVENING"),
                            description = "Study situation tag"
                        ),
                        "text" to Schema.string(description = "Original uplifting UPSC study motivation message (max 220 chars, no song lyrics, not attributed as a real quote)")
                    )
                ),
                description = "List of 14 fresh original study motivation messages"
            )
        )
    )

    private val quoteBatchModel by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = quoteBatchSchema
            }
        )
    }

    fun streamTeacherReply(
        userPrompt: String,
        conversationContext: List<Pair<String, String>> = emptyList()
    ): Flow<String> = flow {
        val historyText = if (conversationContext.isEmpty()) {
            ""
        } else {
            conversationContext.takeLast(6).joinToString("\n") { (role, msg) -> "$role: $msg" } + "\n\n"
        }
        val fullPrompt = "${historyText}Student Question:\n$userPrompt"
        val accumulated = StringBuilder()
        withTimeout(60_000L) {
            chatModel.generateContentStream(fullPrompt).collect { chunk ->
                val part = chunk.text.orEmpty()
                if (part.isNotEmpty()) {
                    accumulated.append(part)
                    emit(accumulated.toString())
                }
            }
        }
        if (accumulated.isBlank()) {
            throw IllegalStateException("AI Teacher returned an empty response.")
        }
    }

    suspend fun evaluateAnswerOrSpeech(prompt: String): Result<String> {
        return try {
            val response = withTimeout(60_000L) {
                chatModel.generateContent(prompt)
            }
            val text = response.text
            if (text.isNullOrBlank()) {
                Result.failure(IllegalStateException("AI Teacher returned an empty evaluation."))
            } else {
                Result.success(text)
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "AI evaluation timed out", e)
            Result.failure(IllegalStateException("Request timed out after 60 seconds. Please try again."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "AI evaluation failed", e)
            Result.failure(e)
        }
    }

    suspend fun generateFreshMotivationBatch(batchNumber: Int): Result<List<MotivationQuote>> {
        val prompt = """
            Create 14 brand-new, original, warm, concise motivational messages (2 for each of RM, JIN, SUGA, JHOPE, JIMIN, V, JUNGKOOK) for an Indian Class 12 Science student preparing for UPSC IAS 2029 with History Optional.
            Match each member's theme:
            RM: Knowledge & leadership; JIN: Confidence; SUGA: Discipline; JHOPE: Positivity; JIMIN: Patience & self-love; V: Creativity & visual thinking; JUNGKOOK: Dedication.
            Do NOT quote copyrighted song lyrics and do NOT pretend these are real spoken quotes. Keep every message fresh, encouraging, and focused on NCERT study, revision, or consistency.
        """.trimIndent()

        return try {
            val response = withTimeout(60_000L) {
                quoteBatchModel.generateContent(prompt)
            }
            val rawJson = response.text ?: throw IllegalStateException("Empty quote batch response.")
            val json = JSONObject(rawJson)
            val arr = json.getJSONArray("quotes")
            val parsed = (0 until arr.length()).map { idx ->
                val obj = arr.getJSONObject(idx)
                val member = BtsMember.safeValueOf(obj.optString("member", "RM"))
                val ctx = obj.optString("contextTag", "SESSION")
                val text = obj.optString("text", "").trim()
                MotivationQuote(
                    quoteId = "ai_b${batchNumber}_${member.name.lowercase()}_$idx",
                    member = member,
                    text = text,
                    labelType = QuoteLabelType.INSPIRED_BY_MEMBER,
                    sourceNote = "AI-crafted fresh motivation inspired by ${member.displayName} (Batch $batchNumber)",
                    contextTag = ctx
                )
            }.filter { it.text.isNotBlank() }

            if (parsed.isEmpty()) {
                Result.failure(IllegalStateException("No valid quotes returned in batch."))
            } else {
                Result.success(parsed)
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Quote batch generation timed out", e)
            Result.failure(IllegalStateException("Quote generation timed out. Please try again."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Quote batch generation failed", e)
            Result.failure(e)
        }
    }
}
