package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_chapters")
data class OfflineChapterEntity(
    @PrimaryKey val chapterId: String,
    val bookId: String,
    val title: String,
    val summaryText: String,
    val downloadedAtEpochMs: Long
)

@Entity(tableName = "local_chapter_progress")
data class LocalChapterProgressEntity(
    @PrimaryKey val chapterId: String,
    val bookId: String,
    val completed: Boolean,
    val downloadedOffline: Boolean,
    val notesRead: Boolean,
    val memoryDone: Boolean,
    val quizDone: Boolean,
    val bestScorePercent: Int,
    val revisionStage: Int,
    val lastRevisedEpochMs: Long,
    val nextRevisionEpochMs: Long,
    val weakConceptsCsv: String
)

@Entity(tableName = "local_study_tasks")
data class LocalStudyTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val titleHi: String,
    val category: String,
    val durationMinutes: Int,
    val actualMinutes: Int,
    val scheduledTime: String,
    val status: String,
    val taskDate: String,
    val custom: Boolean,
    val orderIndex: Int
)

@Entity(tableName = "local_flashcards")
data class LocalFlashcardEntity(
    @PrimaryKey val id: String,
    val deckMember: String,
    val subject: String,
    val chapterId: String,
    val front: String,
    val back: String,
    val explanation: String,
    val difficulty: String,
    val sourceRef: String,
    val masteryStatus: String,
    val favourite: Boolean,
    val reviewCount: Int,
    val intervalDays: Int,
    val nextReviewEpochMs: Long,
    val custom: Boolean
)

@Entity(tableName = "local_quote_history")
data class LocalQuoteHistoryEntity(
    @PrimaryKey val id: String,
    val quoteId: String,
    val member: String,
    val text: String,
    val labelType: String,
    val sourceNote: String,
    val contextTag: String,
    val displayedDate: String,
    val slot: String,
    val favourite: Boolean,
    val createdEpochMs: Long
)

@Entity(tableName = "local_test_attempts")
data class LocalTestAttemptEntity(
    @PrimaryKey val id: String,
    val testTitle: String,
    val testType: String,
    val chapterId: String,
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val skippedCount: Int,
    val netScore: Double,
    val maxScore: Double,
    val accuracyPercent: Int,
    val timeTakenSeconds: Int,
    val weakTopicsCsv: String,
    val createdEpochMs: Long
)

@Entity(tableName = "local_reminders")
data class LocalReminderEntity(
    @PrimaryKey val category: String,
    val title: String,
    val hour: Int,
    val minute: Int,
    val enabled: Boolean
)
