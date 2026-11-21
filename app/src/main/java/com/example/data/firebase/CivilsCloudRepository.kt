package com.example.data.firebase

import android.content.Context
import com.example.R
import com.example.data.local.CivilsDao
import com.example.data.local.LocalChapterProgressEntity
import com.example.data.local.LocalFlashcardEntity
import com.example.data.local.LocalQuoteHistoryEntity
import com.example.data.local.LocalReminderEntity
import com.example.data.local.LocalStudyTaskEntity
import com.example.data.local.LocalTestAttemptEntity
import com.example.data.model.ChapterProgress
import com.example.data.model.EnglishPracticeLog
import com.example.data.model.FlashcardItem
import com.example.data.model.QuoteHistoryEntry
import com.example.data.model.ReminderConfig
import com.example.data.model.StudyTask
import com.example.data.model.TestAttempt
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class CivilsCloudRepository(
    private val db: FirebaseFirestore,
    private val dao: CivilsDao,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context, dao: CivilsDao) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        dao
    )

    private fun requireUserId(): String =
        auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")

    private fun userDoc(uid: String) = db.collection("users").document(uid)

    // ==================== OBSERVE STREAMS ====================

    fun observeUserProfile(uid: String): Flow<UserProfile?> =
        userDoc(uid).snapshots()
            .map { snap ->
                snap.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.GET, "users/$uid", uid)
                throw error
            }

    fun observeChapterProgress(uid: String): Flow<List<ChapterProgress>> =
        userDoc(uid).collection("chapter_progress").snapshots()
            .map { snap ->
                val list = snap.toObjects(ChapterProgress::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                dao.upsertAllChapterProgress(list.map { it.toLocalEntity() })
                list
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/chapter_progress", uid)
                throw error
            }

    fun observeStudyTasks(uid: String): Flow<List<StudyTask>> =
        userDoc(uid).collection("study_tasks").snapshots()
            .map { snap ->
                val list = snap.toObjects(StudyTask::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    .sortedBy { it.orderIndex }
                dao.upsertTasks(list.map { it.toLocalEntity() })
                list
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/study_tasks", uid)
                throw error
            }

    fun observeFlashcards(uid: String): Flow<List<FlashcardItem>> =
        userDoc(uid).collection("flashcards").snapshots()
            .map { snap ->
                val list = snap.toObjects(FlashcardItem::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                dao.upsertFlashcards(list.map { it.toLocalEntity() })
                list
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/flashcards", uid)
                throw error
            }

    fun observeQuoteHistory(uid: String): Flow<List<QuoteHistoryEntry>> =
        userDoc(uid).collection("quote_history").snapshots()
            .map { snap ->
                val list = snap.toObjects(QuoteHistoryEntry::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    .sortedByDescending { it.createdAt?.toDate()?.time ?: 0L }
                dao.upsertAllQuoteHistory(list.map { it.toLocalEntity() })
                list
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/quote_history", uid)
                throw error
            }

    fun observeTestAttempts(uid: String): Flow<List<TestAttempt>> =
        userDoc(uid).collection("test_attempts").snapshots()
            .map { snap ->
                val list = snap.toObjects(TestAttempt::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    .sortedByDescending { it.createdAt?.toDate()?.time ?: 0L }
                dao.upsertAllTestAttempts(list.map { it.toLocalEntity() })
                list
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/test_attempts", uid)
                throw error
            }

    fun observeEnglishLogs(uid: String): Flow<List<EnglishPracticeLog>> =
        userDoc(uid).collection("english_logs").snapshots()
            .map { snap ->
                snap.toObjects(EnglishPracticeLog::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    .sortedByDescending { it.dateStr }
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/english_logs", uid)
                throw error
            }

    fun observeReminderConfigs(uid: String): Flow<List<ReminderConfig>> =
        userDoc(uid).collection("reminder_configs").snapshots()
            .map { snap ->
                val list = snap.toObjects(ReminderConfig::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                dao.upsertReminders(list.map { it.toLocalEntity() })
                list
            }
            .catch { error ->
                unwrapAndLog(error, OperationType.LIST, "users/$uid/reminder_configs", uid)
                throw error
            }

    // ==================== MUTATIONS (Result<T>) ====================

    suspend fun ensureUserProfile(defaultName: String): Result<Unit> {
        val path = "users/${auth.currentUser?.uid ?: "unauth"}"
        return try {
            val uid = requireUserId()
            val docRef = userDoc(uid)
            val existing = docRef.get().await()
            if (!existing.exists()) {
                val profile = UserProfile(
                    userId = uid,
                    displayName = defaultName.ifBlank { "IAS Aspirant 2029" }.take(80)
                )
                docRef.set(profile.toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun updateProfileFields(fields: Map<String, Any>): Result<Unit> {
        val uid = auth.currentUser?.uid
        val path = "users/${uid ?: "unauth"}"
        return try {
            val activeUid = requireUserId()
            val payload = fields.toMutableMap().apply {
                put("updatedAt", FieldValue.serverTimestamp())
            }
            userDoc(activeUid).update(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun upsertChapterProgress(progress: ChapterProgress): Result<Unit> {
        val docId = progress.chapterId.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/chapter_progress/$docId"
        return try {
            val uid = requireUserId()
            dao.upsertChapterProgress(progress.toLocalEntity())
            val docRef = userDoc(uid).collection("chapter_progress").document(docId)
            val snap = docRef.get().await()
            if (snap.exists()) {
                docRef.update(
                    mapOf(
                        "completed" to progress.completed,
                        "downloadedOffline" to progress.downloadedOffline,
                        "notesRead" to progress.notesRead,
                        "memoryDone" to progress.memoryDone,
                        "quizDone" to progress.quizDone,
                        "bestScorePercent" to progress.bestScorePercent.coerceIn(0, 100),
                        "revisionStage" to progress.revisionStage.coerceIn(0, 5),
                        "lastRevisedEpochMs" to progress.lastRevisedEpochMs.coerceAtLeast(0L),
                        "nextRevisionEpochMs" to progress.nextRevisionEpochMs.coerceAtLeast(0L),
                        "weakConcepts" to progress.weakConcepts.take(20).map { it.take(120) },
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(progress.copy(chapterId = docId).toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun upsertStudyTask(task: StudyTask): Result<Unit> {
        val docId = task.id.ifBlank { "task_${System.currentTimeMillis()}" }.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/study_tasks/$docId"
        return try {
            val uid = requireUserId()
            dao.upsertTask(task.copy(id = docId).toLocalEntity())
            val docRef = userDoc(uid).collection("study_tasks").document(docId)
            val snap = docRef.get().await()
            if (snap.exists()) {
                docRef.update(
                    mapOf(
                        "title" to task.title.take(120),
                        "titleHi" to task.titleHi.take(160),
                        "category" to task.category.take(40),
                        "durationMinutes" to task.durationMinutes.coerceIn(5, 480),
                        "actualMinutes" to task.actualMinutes.coerceIn(0, 600),
                        "scheduledTime" to task.scheduledTime.take(20),
                        "status" to task.status,
                        "taskDate" to task.taskDate.take(20),
                        "orderIndex" to task.orderIndex.coerceIn(0, 100),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(task.toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteStudyTask(taskId: String): Result<Unit> {
        val docId = taskId.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/study_tasks/$docId"
        return try {
            val uid = requireUserId()
            dao.deleteTask(docId)
            userDoc(uid).collection("study_tasks").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    suspend fun upsertFlashcard(card: FlashcardItem): Result<Unit> {
        val docId = card.id.ifBlank { "fc_${System.currentTimeMillis()}" }.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/flashcards/$docId"
        return try {
            val uid = requireUserId()
            dao.upsertFlashcard(card.copy(id = docId).toLocalEntity())
            val docRef = userDoc(uid).collection("flashcards").document(docId)
            val snap = docRef.get().await()
            if (snap.exists()) {
                docRef.update(
                    mapOf(
                        "front" to card.front.take(500),
                        "back" to card.back.take(800),
                        "explanation" to card.explanation.take(1200),
                        "difficulty" to card.difficulty,
                        "masteryStatus" to card.masteryStatus,
                        "favourite" to card.favourite,
                        "reviewCount" to card.reviewCount.coerceIn(0, 10000),
                        "intervalDays" to card.intervalDays.coerceIn(0, 365),
                        "nextReviewEpochMs" to card.nextReviewEpochMs.coerceAtLeast(0L),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(card.toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun recordQuoteHistory(entry: QuoteHistoryEntry): Result<Unit> {
        val docId = entry.id.ifBlank { "qh_${entry.quoteId}_${System.currentTimeMillis()}" }
            .replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/quote_history/$docId"
        return try {
            val uid = requireUserId()
            dao.upsertQuoteHistory(entry.copy(id = docId).toLocalEntity())
            userDoc(uid).collection("quote_history").document(docId)
                .set(entry.toCreateMap()).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun toggleQuoteFavourite(entryId: String, favourite: Boolean): Result<Unit> {
        val docId = entryId.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/quote_history/$docId"
        return try {
            val uid = requireUserId()
            userDoc(uid).collection("quote_history").document(docId)
                .update(mapOf("favourite" to favourite, "updatedAt" to FieldValue.serverTimestamp()))
                .await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun recordTestAttempt(attempt: TestAttempt): Result<Unit> {
        val docId = attempt.id.ifBlank { "att_${System.currentTimeMillis()}" }
            .replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/test_attempts/$docId"
        return try {
            val uid = requireUserId()
            dao.upsertTestAttempt(attempt.copy(id = docId).toLocalEntity())
            userDoc(uid).collection("test_attempts").document(docId)
                .set(attempt.toCreateMap()).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun upsertEnglishLog(log: EnglishPracticeLog): Result<Unit> {
        val docId = log.id.ifBlank { "eng_${log.dateStr}" }.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/english_logs/$docId"
        return try {
            val uid = requireUserId()
            val docRef = userDoc(uid).collection("english_logs").document(docId)
            val snap = docRef.get().await()
            if (snap.exists()) {
                docRef.update(
                    mapOf(
                        "minutesPracticed" to log.minutesPracticed.coerceIn(0, 300),
                        "vocabLearnedCount" to log.vocabLearnedCount.coerceIn(0, 500),
                        "mistakesReviewed" to log.mistakesReviewed.coerceIn(0, 200),
                        "confidenceScore" to log.confidenceScore.coerceIn(1, 10),
                        "notes" to log.notes.take(1000),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(log.toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun upsertReminderConfig(config: ReminderConfig): Result<Unit> {
        val docId = config.category.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val path = "users/${auth.currentUser?.uid ?: "unauth"}/reminder_configs/$docId"
        return try {
            val uid = requireUserId()
            dao.upsertReminder(config.toLocalEntity())
            val docRef = userDoc(uid).collection("reminder_configs").document(docId)
            val snap = docRef.get().await()
            if (snap.exists()) {
                docRef.update(
                    mapOf(
                        "title" to config.title.take(80),
                        "hour" to config.hour.coerceIn(0, 23),
                        "minute" to config.minute.coerceIn(0, 59),
                        "enabled" to config.enabled,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(config.toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    private fun unwrapAndLog(error: Throwable, op: OperationType, path: String, uid: String) {
        val firestoreError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == uid) {
            handleFirestoreError(exceptionToLog, op, path)
        }
    }

    private fun ChapterProgress.toLocalEntity() = LocalChapterProgressEntity(
        chapterId = chapterId,
        bookId = bookId,
        completed = completed,
        downloadedOffline = downloadedOffline,
        notesRead = notesRead,
        memoryDone = memoryDone,
        quizDone = quizDone,
        bestScorePercent = bestScorePercent,
        revisionStage = revisionStage,
        lastRevisedEpochMs = lastRevisedEpochMs,
        nextRevisionEpochMs = nextRevisionEpochMs,
        weakConceptsCsv = weakConcepts.joinToString("|")
    )

    private fun StudyTask.toLocalEntity() = LocalStudyTaskEntity(
        id = id,
        title = title,
        titleHi = titleHi,
        category = category,
        durationMinutes = durationMinutes,
        actualMinutes = actualMinutes,
        scheduledTime = scheduledTime,
        status = status,
        taskDate = taskDate,
        custom = custom,
        orderIndex = orderIndex
    )

    private fun FlashcardItem.toLocalEntity() = LocalFlashcardEntity(
        id = id,
        deckMember = deckMember,
        subject = subject,
        chapterId = chapterId,
        front = front,
        back = back,
        explanation = explanation,
        difficulty = difficulty,
        sourceRef = sourceRef,
        masteryStatus = masteryStatus,
        favourite = favourite,
        reviewCount = reviewCount,
        intervalDays = intervalDays,
        nextReviewEpochMs = nextReviewEpochMs,
        custom = custom
    )

    private fun QuoteHistoryEntry.toLocalEntity() = LocalQuoteHistoryEntity(
        id = id,
        quoteId = quoteId,
        member = member,
        text = text,
        labelType = labelType,
        sourceNote = sourceNote,
        contextTag = contextTag,
        displayedDate = displayedDate,
        slot = slot,
        favourite = favourite,
        createdEpochMs = createdAt?.toDate()?.time ?: System.currentTimeMillis()
    )

    private fun TestAttempt.toLocalEntity() = LocalTestAttemptEntity(
        id = id,
        testTitle = testTitle,
        testType = testType,
        chapterId = chapterId,
        totalQuestions = totalQuestions,
        correctCount = correctCount,
        wrongCount = wrongCount,
        skippedCount = skippedCount,
        netScore = netScore,
        maxScore = maxScore,
        accuracyPercent = accuracyPercent,
        timeTakenSeconds = timeTakenSeconds,
        weakTopicsCsv = weakTopics.joinToString("|"),
        createdEpochMs = createdAt?.toDate()?.time ?: System.currentTimeMillis()
    )

    private fun ReminderConfig.toLocalEntity() = LocalReminderEntity(
        category = category,
        title = title,
        hour = hour,
        minute = minute,
        enabled = enabled
    )
}
