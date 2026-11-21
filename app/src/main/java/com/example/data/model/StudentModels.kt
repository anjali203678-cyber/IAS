package com.example.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class ThemeMode {
    LIGHT_LAVENDER,
    DARK_GALAXY;

    companion object {
        fun safeValueOf(raw: String?): ThemeMode =
            entries.firstOrNull { it.name == raw } ?: LIGHT_LAVENDER
    }
}

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    PAUSED,
    COMPLETED;

    companion object {
        fun safeValueOf(raw: String?): TaskStatus =
            entries.firstOrNull { it.name == raw } ?: PENDING
    }
}

enum class FlashcardDifficulty {
    EASY,
    MEDIUM,
    HARD;

    companion object {
        fun safeValueOf(raw: String?): FlashcardDifficulty =
            entries.firstOrNull { it.name == raw } ?: MEDIUM
    }
}

enum class MasteryStatus {
    NEW,
    KNOWN,
    REVISION,
    DIFFICULT;

    companion object {
        fun safeValueOf(raw: String?): MasteryStatus =
            entries.firstOrNull { it.name == raw } ?: NEW
    }
}

@Keep
@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "IAS Aspirant 2029",
    val targetAttemptYear: Int = 2029,
    val syllabusTargetDate: String = "December 2028",
    val optionalSubject: String = "History",
    val primaryLanguage: String = "Hindi",
    val dailyStudyTargetMinutes: Int = 540,
    val englishTargetMinutes: Int = 45,
    val themeMode: String = ThemeMode.LIGHT_LAVENDER.name,
    val favouriteMember: String = "RM",
    val streakDays: Int = 1,
    val totalStars: Int = 7,
    val lastStudyDate: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = buildMap {
        put("userId", userId)
        put("displayName", displayName.take(80).ifBlank { "IAS Aspirant 2029" })
        put("targetAttemptYear", targetAttemptYear.coerceIn(2026, 2035))
        put("syllabusTargetDate", syllabusTargetDate.take(40).ifBlank { "December 2028" })
        put("optionalSubject", optionalSubject.take(60).ifBlank { "History" })
        put("primaryLanguage", primaryLanguage.take(30).ifBlank { "Hindi" })
        put("dailyStudyTargetMinutes", dailyStudyTargetMinutes.coerceIn(60, 960))
        put("englishTargetMinutes", englishTargetMinutes.coerceIn(10, 240))
        put("themeMode", ThemeMode.safeValueOf(themeMode).name)
        put("favouriteMember", favouriteMember.take(30).ifBlank { "RM" })
        put("streakDays", streakDays.coerceIn(0, 5000))
        put("totalStars", totalStars.coerceIn(0, 100000))
        if (lastStudyDate.isNotBlank()) put("lastStudyDate", lastStudyDate.take(20))
        put("createdAt", FieldValue.serverTimestamp())
        put("updatedAt", FieldValue.serverTimestamp())
    }
}

@Keep
@IgnoreExtraProperties
data class ChapterProgress(
    @DocumentId val id: String = "",
    val chapterId: String = "",
    val bookId: String = "",
    val completed: Boolean = false,
    val downloadedOffline: Boolean = false,
    val notesRead: Boolean = false,
    val memoryDone: Boolean = false,
    val quizDone: Boolean = false,
    val bestScorePercent: Int = 0,
    val revisionStage: Int = 0, // 0..5 -> 1d, 3d, 7d, 15d, 30d
    val lastRevisedEpochMs: Long = 0L,
    val nextRevisionEpochMs: Long = 0L,
    val weakConcepts: List<String> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "chapterId" to chapterId.take(64),
        "bookId" to bookId.take(64),
        "completed" to completed,
        "downloadedOffline" to downloadedOffline,
        "notesRead" to notesRead,
        "memoryDone" to memoryDone,
        "quizDone" to quizDone,
        "bestScorePercent" to bestScorePercent.coerceIn(0, 100),
        "revisionStage" to revisionStage.coerceIn(0, 5),
        "lastRevisedEpochMs" to lastRevisedEpochMs.coerceAtLeast(0L),
        "nextRevisionEpochMs" to nextRevisionEpochMs.coerceAtLeast(0L),
        "weakConcepts" to weakConcepts.take(20).map { it.take(120) },
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class StudyTask(
    @DocumentId val id: String = "",
    val title: String = "",
    val titleHi: String = "",
    val category: String = "FOUNDATION",
    val durationMinutes: Int = 60,
    val actualMinutes: Int = 0,
    val scheduledTime: String = "06:00",
    val status: String = TaskStatus.PENDING.name,
    val taskDate: String = "",
    val custom: Boolean = false,
    val orderIndex: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "title" to title.take(120).ifBlank { "Study Block" },
        "titleHi" to titleHi.take(160),
        "category" to category.take(40).ifBlank { "FOUNDATION" },
        "durationMinutes" to durationMinutes.coerceIn(5, 480),
        "actualMinutes" to actualMinutes.coerceIn(0, 600),
        "scheduledTime" to scheduledTime.take(20).ifBlank { "06:00" },
        "status" to TaskStatus.safeValueOf(status).name,
        "taskDate" to taskDate.take(20).ifBlank { "2026-11-21" },
        "custom" to custom,
        "orderIndex" to orderIndex.coerceIn(0, 100),
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class FlashcardItem(
    @DocumentId val id: String = "",
    val deckMember: String = "RM",
    val subject: String = "History",
    val chapterId: String = "ncert_hist_6_ch1",
    val front: String = "",
    val back: String = "",
    val explanation: String = "",
    val difficulty: String = FlashcardDifficulty.MEDIUM.name,
    val sourceRef: String = "",
    val masteryStatus: String = MasteryStatus.NEW.name,
    val favourite: Boolean = false,
    val reviewCount: Int = 0,
    val intervalDays: Int = 1,
    val nextReviewEpochMs: Long = 0L,
    val custom: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "deckMember" to deckMember.take(30).ifBlank { "RM" },
        "subject" to subject.take(60).ifBlank { "History" },
        "chapterId" to chapterId.take(64).ifBlank { "general" },
        "front" to front.take(500).ifBlank { "Concept" },
        "back" to back.take(800).ifBlank { "Answer" },
        "explanation" to explanation.take(1200),
        "difficulty" to FlashcardDifficulty.safeValueOf(difficulty).name,
        "sourceRef" to sourceRef.take(200),
        "masteryStatus" to MasteryStatus.safeValueOf(masteryStatus).name,
        "favourite" to favourite,
        "reviewCount" to reviewCount.coerceIn(0, 10000),
        "intervalDays" to intervalDays.coerceIn(0, 365),
        "nextReviewEpochMs" to nextReviewEpochMs.coerceAtLeast(0L),
        "custom" to custom,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class QuoteHistoryEntry(
    @DocumentId val id: String = "",
    val quoteId: String = "",
    val member: String = "RM",
    val text: String = "",
    val labelType: String = "INSPIRED_BY_MEMBER",
    val sourceNote: String = "",
    val contextTag: String = "MORNING",
    val displayedDate: String = "",
    val slot: String = "MORNING",
    val favourite: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "quoteId" to quoteId.take(64).ifBlank { "q_1" },
        "member" to member.take(30).ifBlank { "RM" },
        "text" to text.take(600).ifBlank { "Keep moving forward." },
        "labelType" to if (labelType == "VERIFIED_BTS_QUOTE") "VERIFIED_BTS_QUOTE" else "INSPIRED_BY_MEMBER",
        "sourceNote" to sourceNote.take(200),
        "contextTag" to contextTag.take(40).ifBlank { "MORNING" },
        "displayedDate" to displayedDate.take(20).ifBlank { "2026-11-21" },
        "slot" to slot.ifBlank { "MORNING" },
        "favourite" to favourite,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class TestAttempt(
    @DocumentId val id: String = "",
    val testTitle: String = "",
    val testType: String = "CHAPTER_TEST",
    val chapterId: String = "",
    val totalQuestions: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val skippedCount: Int = 0,
    val netScore: Double = 0.0,
    val maxScore: Double = 0.0,
    val accuracyPercent: Int = 0,
    val timeTakenSeconds: Int = 0,
    val weakTopics: List<String> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "testTitle" to testTitle.take(120).ifBlank { "Practice Test" },
        "testType" to testType.take(40).ifBlank { "CHAPTER_TEST" },
        "chapterId" to chapterId.take(64),
        "totalQuestions" to totalQuestions.coerceIn(1, 300),
        "correctCount" to correctCount.coerceIn(0, 300),
        "wrongCount" to wrongCount.coerceIn(0, 300),
        "skippedCount" to skippedCount.coerceIn(0, 300),
        "netScore" to netScore,
        "maxScore" to maxScore,
        "accuracyPercent" to accuracyPercent.coerceIn(0, 100),
        "timeTakenSeconds" to timeTakenSeconds.coerceIn(0, 86400),
        "weakTopics" to weakTopics.take(20).map { it.take(120) },
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class EnglishPracticeLog(
    @DocumentId val id: String = "",
    val dateStr: String = "",
    val minutesPracticed: Int = 0,
    val vocabLearnedCount: Int = 0,
    val mistakesReviewed: Int = 0,
    val confidenceScore: Int = 5,
    val notes: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "dateStr" to dateStr.take(20).ifBlank { "2026-11-21" },
        "minutesPracticed" to minutesPracticed.coerceIn(0, 300),
        "vocabLearnedCount" to vocabLearnedCount.coerceIn(0, 500),
        "mistakesReviewed" to mistakesReviewed.coerceIn(0, 200),
        "confidenceScore" to confidenceScore.coerceIn(1, 10),
        "notes" to notes.take(1000),
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class ReminderConfig(
    @DocumentId val id: String = "",
    val category: String = "",
    val title: String = "",
    val hour: Int = 6,
    val minute: Int = 0,
    val enabled: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "category" to category.take(40).ifBlank { "MORNING_START" },
        "title" to title.take(80).ifBlank { "Study Reminder" },
        "hour" to hour.coerceIn(0, 23),
        "minute" to minute.coerceIn(0, 59),
        "enabled" to enabled,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}
