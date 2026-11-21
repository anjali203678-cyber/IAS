package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.AiTeacherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatTurn(
    val id: String,
    val isUser: Boolean,
    val text: String
)

sealed interface AiEvalState {
    data object Idle : AiEvalState
    data object Loading : AiEvalState
    data class Success(val feedback: String) : AiEvalState
    data class Error(val message: String) : AiEvalState
}

class AiTeacherViewModel(
    private val aiRepo: AiTeacherRepository = AiTeacherRepository()
) : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(
            ChatTurn(
                id = "welcome_1",
                isUser = false,
                text = "नमस्ते! मैं आपका CIVILS IAS AI Teacher हूँ। 💜\n" +
                    "हम बिलकुल शुरुआत (Class 6 NCERT से) पढ़ेंगे—आसान हिंदी में, और धीरे-धीरे ज़रूरी English UPSC शब्द सीखेंगे।\n\n" +
                    "आज क्या पढ़ना या दोहराना चाहेंगी? (जैसे: 'NCERT History Ch 1 समझाओ', 'Manuscript vs Inscription के लिए Memory Trick दो', या 'Oral Revision शुरू करो!')"
            )
        )
    )
    val messages: StateFlow<List<ChatTurn>> = _messages.asStateFlow()

    private val _streamingReply = MutableStateFlow("")
    val streamingReply: StateFlow<String> = _streamingReply.asStateFlow()

    private val _isThinking = MutableStateFlow(false)
    val isThinking: StateFlow<Boolean> = _isThinking.asStateFlow()

    private val _chatError = MutableStateFlow<String?>(null)
    val chatError: StateFlow<String?> = _chatError.asStateFlow()

    private val _evalState = MutableStateFlow<AiEvalState>(AiEvalState.Idle)
    val evalState: StateFlow<AiEvalState> = _evalState.asStateFlow()

    fun sendMessage(prompt: String, modeHint: String = "TEACH") {
        val clean = prompt.trim()
        if (clean.isEmpty() || _isThinking.value) return

        _chatError.value = null
        val userTurn = ChatTurn(id = "u_${System.currentTimeMillis()}", isUser = true, text = clean)
        val priorHistory = _messages.value.map { (if (it.isUser) "Student" else "Teacher") to it.text }
        _messages.value = _messages.value + userTurn
        _isThinking.value = true
        _streamingReply.value = ""

        val enrichedPrompt = when (modeHint) {
            "MEMORY" -> "Create easy Hindi + visual mnemonics, acronyms, or a short story to remember this UPSC concept:\n$clean"
            "ORAL_REVISION" -> "Conduct a friendly 1-question oral active-recall revision in simple Hindi (with English terms) on:\n$clean"
            "ENGLISH_PRACTICE" -> "Help me practice English speaking for UPSC. Gently correct my grammar if needed, give a natural improved version, and explain in Hindi:\n$clean"
            else -> clean
        }

        viewModelScope.launch {
            var latest = ""
            try {
                aiRepo.streamTeacherReply(enrichedPrompt, priorHistory).collect { partial ->
                    latest = partial
                    _streamingReply.value = partial
                }
                if (latest.isNotBlank()) {
                    _messages.value = _messages.value + ChatTurn(
                        id = "ai_${System.currentTimeMillis()}",
                        isUser = false,
                        text = latest
                    )
                }
            } catch (e: TimeoutCancellationException) {
                Log.e("AiTeacherVM", "Stream timed out", e)
                _chatError.value = "AI Teacher timed out after 60 seconds. Please try again."
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("AiTeacherVM", "AI stream failed", e)
                _chatError.value = e.localizedMessage ?: "Failed to reach Gemini AI Teacher."
            } finally {
                _streamingReply.value = ""
                _isThinking.value = false
            }
        }
    }

    fun evaluateWrittenResponse(question: String, studentAnswer: String, rubricType: String) {
        if (studentAnswer.isBlank()) {
            _evalState.value = AiEvalState.Error("Please write your answer before requesting evaluation.")
            return
        }
        val prompt = """
            Evaluate this UPSC $rubricType response for a beginner IAS 2029 aspirant.
            Explain feedback warmly in simple Hindi with English UPSC terms.
            Provide: 1) Score estimate, 2) Introduction/Body/Conclusion check, 3) Strengths, 4) Specific improvements, 5) Weak concepts to revise.
            Question: $question
            Student Response: $studentAnswer
        """.trimIndent()

        viewModelScope.launch {
            _evalState.value = AiEvalState.Loading
            aiRepo.evaluateAnswerOrSpeech(prompt)
                .onSuccess { _evalState.value = AiEvalState.Success(it) }
                .onFailure { _evalState.value = AiEvalState.Error(it.localizedMessage ?: "Evaluation failed.") }
        }
    }

    fun clearEval() {
        _evalState.value = AiEvalState.Idle
    }
}
