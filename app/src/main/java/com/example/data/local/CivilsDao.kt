package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CivilsDao {

    // Offline downloaded chapters
    @Query("SELECT * FROM offline_chapters")
    fun observeOfflineChapters(): Flow<List<OfflineChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOfflineChapter(chapter: OfflineChapterEntity)

    @Query("DELETE FROM offline_chapters WHERE chapterId = :chapterId")
    suspend fun deleteOfflineChapter(chapterId: String)

    // Chapter progress
    @Query("SELECT * FROM local_chapter_progress")
    fun observeAllChapterProgress(): Flow<List<LocalChapterProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChapterProgress(progress: LocalChapterProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllChapterProgress(items: List<LocalChapterProgressEntity>)

    // Study tasks
    @Query("SELECT * FROM local_study_tasks WHERE taskDate = :taskDate ORDER BY orderIndex ASC")
    fun observeTasksForDate(taskDate: String): Flow<List<LocalStudyTaskEntity>>

    @Query("SELECT * FROM local_study_tasks ORDER BY taskDate DESC, orderIndex ASC")
    fun observeAllTasks(): Flow<List<LocalStudyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: LocalStudyTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTasks(tasks: List<LocalStudyTaskEntity>)

    @Query("DELETE FROM local_study_tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    // Flashcards
    @Query("SELECT * FROM local_flashcards")
    fun observeAllFlashcards(): Flow<List<LocalFlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFlashcard(card: LocalFlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFlashcards(cards: List<LocalFlashcardEntity>)

    // Quote history
    @Query("SELECT * FROM local_quote_history ORDER BY createdEpochMs DESC")
    fun observeQuoteHistory(): Flow<List<LocalQuoteHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuoteHistory(entry: LocalQuoteHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllQuoteHistory(entries: List<LocalQuoteHistoryEntity>)

    // Test attempts
    @Query("SELECT * FROM local_test_attempts ORDER BY createdEpochMs DESC")
    fun observeTestAttempts(): Flow<List<LocalTestAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTestAttempt(attempt: LocalTestAttemptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllTestAttempts(attempts: List<LocalTestAttemptEntity>)

    // Reminders
    @Query("SELECT * FROM local_reminders")
    fun observeReminders(): Flow<List<LocalReminderEntity>>

    @Query("SELECT * FROM local_reminders WHERE enabled = 1")
    suspend fun getEnabledRemindersOnce(): List<LocalReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReminder(reminder: LocalReminderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReminders(reminders: List<LocalReminderEntity>)
}
