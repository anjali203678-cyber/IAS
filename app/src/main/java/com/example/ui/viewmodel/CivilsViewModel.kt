package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.AiTeacherRepository
import com.example.data.firebase.CivilsCloudRepository
import com.example.data.local.AppPreferences
import com.example.data.local.CivilsDao
import com.example.data.local.OfflineChapterEntity
import com.example.data.model.BtsMember
import com.example.data.model.ChapterProgress
import com.example.data.model.ChapterQuestion
import com.example.data.model.ContentStatus
import com.example.data.model.EnglishPracticeLog
import com.example.data.model.FlashcardDifficulty
import com.example.data.model.FlashcardItem
import com.example.data.model.MasteryStatus
import com.example.data.model.MotivationQuote
import com.example.data.model.QuoteHistoryEntry
import com.example.data.model.QuoteSlot
import com.example.data.model.ReminderCategory
import com.example.data.model.ReminderConfig
import com.example.data.model.StudyTask
import com.example.data.model.TaskStatus
import com.example.data.model.TestAttempt
import com.example.data.model.ThemeMode
import com.example.data.model.UiState
import com.example.data.model.UserProfile
import com.example.data.seed.AcademiesSeed
import com.example.data.seed.FlashcardSeed
import com.example.data.seed.HistoryCh1NotesSeed
import com.example.data.seed.MotivationSeed
import com.example.data.seed.NcertCatalogSeed
import com.example.data.seed.QuoteSelectionEngine
import com.example.notifications.StudyReminderScheduler
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ActiveMotivationBanner(
    val quote: MotivationQuote,
    val historyEntryId: String?,
    val isFavourite: Boolean,
    val collectionExhausted: Boolean,
    val remainingUnseenCount: Int
)

class CivilsViewModel(
    private val cloudRepo: CivilsCloudRepository,
    private val dao: CivilsDao,
    private val prefs: AppPreferences,
    private val aiRepo: AiTeacherRepository,
    private val currentUserId: String,
    private val defaultDisplayName: String
) : ViewModel() {

    val todayDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    private val _statusBanner = MutableStateFlow<String?>(null)
    val statusBanner: StateFlow<String?> = _statusBanner.asStateFlow()

    private val _activeMotivation = MutableStateFlow<ActiveMotivationBanner?>(null)
    val activeMotivation: StateFlow<ActiveMotivationBanner?> = _activeMotivation.asStateFlow()

    private val _isGeneratingQuotes = MutableStateFlow(false)
    val isGeneratingQuotes: StateFlow<Boolean> = _isGeneratingQuotes.asStateFlow()

    private val _activeTimerTaskId = MutableStateFlow<String?>(null)
    val activeTimerTaskId: StateFlow<String?> = _activeTimerTaskId.asStateFlow()

    private val _activeTimerElapsedSeconds = MutableStateFlow(0)
    val activeTimerElapsedSeconds: StateFlow<Int> = _activeTimerElapsedSeconds.asStateFlow()

    private var timerJob: Job? = null
    private var seededInitialUserData = false

    val themeMode: StateFlow<ThemeMode> = prefs.themeModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ThemeMode.LIGHT_LAVENDER)

    val lastSyncEpoch: StateFlow<Long> = prefs.lastSyncEpochFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0L)

    val dailyFlashcardTarget: StateFlow<Int> = prefs.dailyFlashcardTargetFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 15)

    val quotePool: StateFlow<List<MotivationQuote>> = prefs.extraQuotesFlow
        .map { extra -> MotivationSeed.initialQuotes + extra }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), MotivationSeed.initialQuotes)

    val offlineChapterIds: StateFlow<Set<String>> = dao.observeOfflineChapters()
        .map { list -> list.map { it.chapterId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptySet())

    val profileState: StateFlow<UiState<UserProfile>> = cloudRepo.observeUserProfile(currentUserId)
        .map<UserProfile?, UiState<UserProfile>> { prof ->
            UiState.Success(prof ?: UserProfile(userId = currentUserId, displayName = defaultDisplayName))
        }
        .unwrapFirestoreErrors("Failed to load profile")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val chapterProgressState: StateFlow<UiState<List<ChapterProgress>>> =
        cloudRepo.observeChapterProgress(currentUserId)
            .map<List<ChapterProgress>, UiState<List<ChapterProgress>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load chapter progress")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val studyTasksState: StateFlow<UiState<List<StudyTask>>> =
        cloudRepo.observeStudyTasks(currentUserId)
            .map<List<StudyTask>, UiState<List<StudyTask>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load study planner")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val flashcardsState: StateFlow<UiState<List<FlashcardItem>>> =
        cloudRepo.observeFlashcards(currentUserId)
            .map<List<FlashcardItem>, UiState<List<FlashcardItem>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load flashcards")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val quoteHistoryState: StateFlow<UiState<List<QuoteHistoryEntry>>> =
        cloudRepo.observeQuoteHistory(currentUserId)
            .map<List<QuoteHistoryEntry>, UiState<List<QuoteHistoryEntry>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load motivation history")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val testAttemptsState: StateFlow<UiState<List<TestAttempt>>> =
        cloudRepo.observeTestAttempts(currentUserId)
            .map<List<TestAttempt>, UiState<List<TestAttempt>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load test attempts")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val englishLogsState: StateFlow<UiState<List<EnglishPracticeLog>>> =
        cloudRepo.observeEnglishLogs(currentUserId)
            .map<List<EnglishPracticeLog>, UiState<List<EnglishPracticeLog>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load English logs")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val remindersState: StateFlow<UiState<List<ReminderConfig>>> =
        cloudRepo.observeReminderConfigs(currentUserId)
            .map<List<ReminderConfig>, UiState<List<ReminderConfig>>> { UiState.Success(it) }
            .unwrapFirestoreErrors("Failed to load reminder settings")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    init {
        bootstrapStudentWorkspace()
    }

    private fun bootstrapStudentWorkspace() {
        viewModelScope.launch {
            cloudRepo.ensureUserProfile(defaultDisplayName)
                .onFailure { _statusBanner.value = "Offline mode active — will sync when online." }

            // Wait briefly for initial snapshots and seed defaults if empty
            combine(studyTasksState, flashcardsState, remindersState, quoteHistoryState) { t, f, r, q ->
                listOf(t, f, r, q)
            }.collect { states ->
                if (seededInitialUserData) return@collect
                val tasksReady = states[0] is UiState.Success
                val cardsReady = states[1] is UiState.Success
                val remindersReady = states[2] is UiState.Success
                val quotesReady = states[3] is UiState.Success
                if (tasksReady && cardsReady && remindersReady && quotesReady) {
                    seededInitialUserData = true
                    val existingTasks = (states[0] as UiState.Success<List<StudyTask>>).data
                    if (existingTasks.none { it.taskDate == todayDate }) {
                        AcademiesSeed.defaultNineHourStudyPlan(todayDate).forEach {
                            cloudRepo.upsertStudyTask(it)
                        }
                    }
                    val existingCards = (states[1] as UiState.Success<List<FlashcardItem>>).data
                    if (existingCards.isEmpty()) {
                        FlashcardSeed.starterCards.forEach { cloudRepo.upsertFlashcard(it) }
                    }
                    val existingReminders = (states[2] as UiState.Success<List<ReminderConfig>>).data
                    if (existingReminders.isEmpty()) {
                        ReminderCategory.entries.forEach { cat ->
                            cloudRepo.upsertReminderConfig(
                                ReminderConfig(
                                    category = cat.id,
                                    title = cat.title,
                                    hour = cat.defaultHour,
                                    minute = cat.defaultMinute,
                                    enabled = false
                                )
                            )
                        }
                    }
                    val history = (states[3] as UiState.Success<List<QuoteHistoryEntry>>).data
                    val todayMorning = history.firstOrNull { it.displayedDate == todayDate && it.slot == QuoteSlot.MORNING.name }
                    if (todayMorning != null) {
                        _activeMotivation.value = ActiveMotivationBanner(
                            quote = MotivationQuote(
                                quoteId = todayMorning.quoteId,
                                member = BtsMember.safeValueOf(todayMorning.member),
                                text = todayMorning.text,
                                labelType = if (todayMorning.labelType == "VERIFIED_BTS_QUOTE") {
                                    com.example.data.model.QuoteLabelType.VERIFIED_BTS_QUOTE
                                } else {
                                    com.example.data.model.QuoteLabelType.INSPIRED_BY_MEMBER
                                },
                                sourceNote = todayMorning.sourceNote,
                                contextTag = todayMorning.contextTag
                            ),
                            historyEntryId = todayMorning.id,
                            isFavourite = todayMorning.favourite,
                            collectionExhausted = history.map { it.quoteId }.toSet().size >= quotePool.value.size,
                            remainingUnseenCount = (quotePool.value.size - history.map { it.quoteId }.toSet().size).coerceAtLeast(0)
                        )
                    } else {
                        showNextMotivation(slot = QuoteSlot.MORNING, preferredContext = "MORNING")
                    }
                    prefs.setLastSyncEpoch(System.currentTimeMillis())
                }
            }
        }
    }

    fun clearBanner() {
        _statusBanner.value = null
    }

    fun toggleThemeMode() {
        viewModelScope.launch {
            val next = if (themeMode.value == ThemeMode.LIGHT_LAVENDER) {
                ThemeMode.DARK_GALAXY
            } else {
                ThemeMode.LIGHT_LAVENDER
            }
            prefs.setThemeMode(next)
            cloudRepo.updateProfileFields(mapOf("themeMode" to next.name))
        }
    }

    fun selectFavouriteMember(member: BtsMember) {
        viewModelScope.launch {
            cloudRepo.updateProfileFields(mapOf("favouriteMember" to member.name))
                .onSuccess { _statusBanner.value = "Favourite BTS member set to ${member.displayName}!" }
                .onFailure { _statusBanner.value = it.localizedMessage ?: "Failed to save preference." }
        }
    }

    // ==================== NON-REPEATING MOTIVATION ENGINE ====================

    fun showNextMotivation(
        slot: QuoteSlot = QuoteSlot.SHUFFLE,
        preferredContext: String? = null,
        preferredMember: BtsMember? = null
    ) {
        viewModelScope.launch {
            val history = (quoteHistoryState.value as? UiState.Success)?.data ?: emptyList()
            val pool = quotePool.value
            val result = QuoteSelectionEngine.selectNextQuote(
                pool = pool,
                history = history,
                todayDate = todayDate,
                slot = slot,
                preferredContext = preferredContext,
                preferredMember = preferredMember
            )
            val entryId = "qh_${result.quote.quoteId}_${System.currentTimeMillis()}"
            val entry = QuoteHistoryEntry(
                id = entryId,
                quoteId = result.quote.quoteId,
                member = result.quote.member.name,
                text = result.quote.text,
                labelType = result.quote.labelType.name,
                sourceNote = result.quote.sourceNote,
                contextTag = result.quote.contextTag,
                displayedDate = todayDate,
                slot = slot.name,
                favourite = false
            )
            _activeMotivation.value = ActiveMotivationBanner(
                quote = result.quote,
                historyEntryId = entryId,
                isFavourite = false,
                collectionExhausted = result.collectionExhausted,
                remainingUnseenCount = result.remainingUnseenCount
            )
            cloudRepo.recordQuoteHistory(entry)
        }
    }

    fun toggleCurrentMotivationFavourite() {
        val current = _activeMotivation.value ?: return
        val entryId = current.historyEntryId ?: return
        val nextFav = !current.isFavourite
        _activeMotivation.value = current.copy(isFavourite = nextFav)
        viewModelScope.launch {
            cloudRepo.toggleQuoteFavourite(entryId, nextFav)
        }
    }

    fun toggleQuoteHistoryFavourite(entry: QuoteHistoryEntry) {
        viewModelScope.launch {
            cloudRepo.toggleQuoteFavourite(entry.id, !entry.favourite)
        }
    }

    fun generateNewMotivationBatchWithAi() {
        if (_isGeneratingQuotes.value) return
        viewModelScope.launch {
            _isGeneratingQuotes.value = true
            val batchNum = ((quotePool.value.size - MotivationSeed.initialQuotes.size) / 14) + 2
            aiRepo.generateFreshMotivationBatch(batchNum)
                .onSuccess { fresh ->
                    prefs.appendExtraQuotes(fresh)
                    _statusBanner.value = "Added ${fresh.size} brand-new non-repeating motivational messages!"
                    showNextMotivation(slot = QuoteSlot.SHUFFLE)
                }
                .onFailure { e ->
                    _statusBanner.value = "Could not generate new batch: ${e.localizedMessage}"
                }
            _isGeneratingQuotes.value = false
        }
    }

    // ==================== CHAPTERS, OFFLINE PACKS & SPACED REPETITION ====================

    fun toggleOfflineDownload(chapterId: String) {
        viewModelScope.launch {
            val catalogItem = NcertCatalogSeed.findChapter(chapterId) ?: return@launch
            if (catalogItem.contentStatus != ContentStatus.FULL_CONTENT) {
                _statusBanner.value = "Detailed notes for this chapter are not authored yet — offline download unavailable."
                return@launch
            }
            val isAlready = chapterId in offlineChapterIds.value
            val currentProg = ((chapterProgressState.value as? UiState.Success)?.data ?: emptyList())
                .firstOrNull { it.chapterId == chapterId }
                ?: ChapterProgress(chapterId = chapterId, bookId = catalogItem.bookId)

            if (isAlready) {
                dao.deleteOfflineChapter(chapterId)
                cloudRepo.upsertChapterProgress(currentProg.copy(downloadedOffline = false))
                _statusBanner.value = "Removed offline study pack for ${catalogItem.title}."
            } else {
                val summary = HistoryCh1NotesSeed.chapterContent.chapterSummaryHi
                dao.upsertOfflineChapter(
                    OfflineChapterEntity(
                        chapterId = chapterId,
                        bookId = catalogItem.bookId,
                        title = catalogItem.title,
                        summaryText = summary,
                        downloadedAtEpochMs = System.currentTimeMillis()
                    )
                )
                cloudRepo.upsertChapterProgress(currentProg.copy(downloadedOffline = true))
                _statusBanner.value = "Saved ${catalogItem.title} for offline study!"
            }
        }
    }

    fun markChapterSectionProgress(
        chapterId: String,
        notesRead: Boolean? = null,
        memoryDone: Boolean? = null,
        quizDone: Boolean? = null,
        markComplete: Boolean? = null
    ) {
        viewModelScope.launch {
            val chapter = NcertCatalogSeed.findChapter(chapterId) ?: return@launch
            if (markComplete == true && chapter.contentStatus != ContentStatus.FULL_CONTENT) {
                _statusBanner.value = "Cannot mark chapter completed: detailed learning content is missing."
                return@launch
            }
            val existing = ((chapterProgressState.value as? UiState.Success)?.data ?: emptyList())
                .firstOrNull { it.chapterId == chapterId }
                ?: ChapterProgress(chapterId = chapterId, bookId = chapter.bookId)

            val now = System.currentTimeMillis()
            val intervalsDays = listOf(1, 3, 7, 15, 30)
            val updatedCompleted = markComplete ?: existing.completed
            val nextStage = if (updatedCompleted && existing.revisionStage == 0) 1 else existing.revisionStage
            val intervalDays = intervalsDays.getOrElse((nextStage - 1).coerceAtLeast(0)) { 30 }
            val nextRev = if (updatedCompleted) now + intervalDays * 86_400_000L else existing.nextRevisionEpochMs

            val updated = existing.copy(
                notesRead = notesRead ?: existing.notesRead,
                memoryDone = memoryDone ?: existing.memoryDone,
                quizDone = quizDone ?: existing.quizDone,
                completed = updatedCompleted,
                revisionStage = nextStage,
                lastRevisedEpochMs = if (updatedCompleted) now else existing.lastRevisedEpochMs,
                nextRevisionEpochMs = nextRev
            )
            cloudRepo.upsertChapterProgress(updated)
            if (updatedCompleted && !existing.completed) {
                awardStars(5)
                showNextMotivation(slot = QuoteSlot.CELEBRATION, preferredContext = "CHAPTER_COMPLETE")
                _statusBanner.value = "Chapter completed! +5 Purple Stars & Day-1 revision scheduled."
            }
        }
    }

    fun advanceSpacedRevision(progress: ChapterProgress) {
        viewModelScope.launch {
            val intervals = listOf(1, 3, 7, 15, 30)
            val now = System.currentTimeMillis()
            // Adjust slower if score < 60%
            val nextStage = if (progress.bestScorePercent in 1..59) {
                progress.revisionStage.coerceAtLeast(1)
            } else {
                (progress.revisionStage + 1).coerceAtMost(5)
            }
            val days = intervals.getOrElse(nextStage - 1) { 30 }
            cloudRepo.upsertChapterProgress(
                progress.copy(
                    revisionStage = nextStage,
                    lastRevisedEpochMs = now,
                    nextRevisionEpochMs = now + days * 86_400_000L
                )
            )
            awardStars(2)
            _statusBanner.value = "Revision logged! Next reminder in $days day(s)."
        }
    }

    fun generateSuggestedFlashcardsForChapter(chapterId: String) {
        viewModelScope.launch {
            if (chapterId != "ncert_hist_6_ch1") {
                _statusBanner.value = "Suggested flashcards generate automatically once chapter notes are authored."
                return@launch
            }
            val existingFronts = ((flashcardsState.value as? UiState.Success)?.data ?: emptyList())
                .map { it.front }.toSet()
            val candidates = HistoryCh1NotesSeed.chapterContent.starterFlashcards
                .filter { it.front !in existingFronts }
            if (candidates.isEmpty()) {
                _statusBanner.value = "All starter flashcards for this chapter are already in your deck!"
            } else {
                candidates.forEach { cloudRepo.upsertFlashcard(it) }
                _statusBanner.value = "Added ${candidates.size} suggested flashcards from Chapter 1!"
            }
        }
    }

    // ==================== 9-HOUR PLANNER & LIVE TIMER ====================

    fun startOrResumeTaskTimer(task: StudyTask) {
        if (_activeTimerTaskId.value == task.id) return
        timerJob?.cancel()
        _activeTimerTaskId.value = task.id
        _activeTimerElapsedSeconds.value = task.actualMinutes * 60
        viewModelScope.launch {
            cloudRepo.upsertStudyTask(task.copy(status = TaskStatus.IN_PROGRESS.name))
        }
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val nextSec = _activeTimerElapsedSeconds.value + 1
                _activeTimerElapsedSeconds.value = nextSec
                if (nextSec % 60 == 0) {
                    val mins = nextSec / 60
                    cloudRepo.upsertStudyTask(
                        task.copy(
                            status = TaskStatus.IN_PROGRESS.name,
                            actualMinutes = mins
                        )
                    )
                }
            }
        }
    }

    fun pauseActiveTask(task: StudyTask) {
        if (_activeTimerTaskId.value == task.id) {
            timerJob?.cancel()
            _activeTimerTaskId.value = null
        }
        val mins = (_activeTimerElapsedSeconds.value / 60).coerceAtLeast(task.actualMinutes)
        viewModelScope.launch {
            cloudRepo.upsertStudyTask(
                task.copy(status = TaskStatus.PAUSED.name, actualMinutes = mins)
            )
            showNextMotivation(slot = QuoteSlot.BREAK, preferredContext = "BREAK")
        }
    }

    fun completeStudyTask(task: StudyTask) {
        if (_activeTimerTaskId.value == task.id) {
            timerJob?.cancel()
            _activeTimerTaskId.value = null
        }
        val loggedMinutes = maxOf(task.actualMinutes, task.durationMinutes)
        viewModelScope.launch {
            cloudRepo.upsertStudyTask(
                task.copy(status = TaskStatus.COMPLETED.name, actualMinutes = loggedMinutes)
            )
            val allTasks = ((studyTasksState.value as? UiState.Success)?.data ?: emptyList())
                .filter { it.taskDate == todayDate }
            val completedAfter = allTasks.sumOf {
                if (it.id == task.id) loggedMinutes else if (it.status == TaskStatus.COMPLETED.name) it.actualMinutes else 0
            }
            if (completedAfter >= 540) {
                awardStars(7)
                showNextMotivation(slot = QuoteSlot.CELEBRATION, preferredContext = "GOAL_COMPLETE")
                _statusBanner.value = "9-Hour Daily Goal Achieved! +7 Purple Stars!"
            } else {
                awardStars(1)
            }
        }
    }

    fun saveCustomOrEditedTask(
        existingTask: StudyTask?,
        title: String,
        category: String,
        durationMinutes: Int,
        scheduledTime: String
    ) {
        viewModelScope.launch {
            val currentCount = ((studyTasksState.value as? UiState.Success)?.data ?: emptyList())
                .count { it.taskDate == todayDate }
            val task = existingTask?.copy(
                title = title.trim(),
                category = category,
                durationMinutes = durationMinutes.coerceIn(5, 480),
                scheduledTime = scheduledTime.trim()
            ) ?: StudyTask(
                id = "custom_${System.currentTimeMillis()}",
                title = title.trim(),
                titleHi = title.trim(),
                category = category,
                durationMinutes = durationMinutes.coerceIn(5, 480),
                actualMinutes = 0,
                scheduledTime = scheduledTime.trim(),
                status = TaskStatus.PENDING.name,
                taskDate = todayDate,
                custom = true,
                orderIndex = currentCount
            )
            cloudRepo.upsertStudyTask(task)
                .onSuccess { _statusBanner.value = "Study block saved!" }
                .onFailure { _statusBanner.value = it.localizedMessage ?: "Failed to save task." }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            cloudRepo.deleteStudyTask(taskId)
        }
    }

    // ==================== FLASHCARDS SPACED REPETITION ====================

    fun reviewFlashcard(card: FlashcardItem, newStatus: MasteryStatus) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val nextIntervalDays = when (newStatus) {
                MasteryStatus.KNOWN -> when {
                    card.intervalDays < 3 -> 3
                    card.intervalDays < 7 -> 7
                    card.intervalDays < 15 -> 15
                    else -> 30
                }
                MasteryStatus.REVISION -> 1
                MasteryStatus.DIFFICULT -> 1
                MasteryStatus.NEW -> 1
            }
            cloudRepo.upsertFlashcard(
                card.copy(
                    masteryStatus = newStatus.name,
                    reviewCount = card.reviewCount + 1,
                    intervalDays = nextIntervalDays,
                    nextReviewEpochMs = now + nextIntervalDays * 86_400_000L
                )
            )
        }
    }

    fun toggleFlashcardFavourite(card: FlashcardItem) {
        viewModelScope.launch {
            cloudRepo.upsertFlashcard(card.copy(favourite = !card.favourite))
        }
    }

    fun saveCustomFlashcard(
        existing: FlashcardItem?,
        member: BtsMember,
        subject: String,
        front: String,
        back: String,
        explanation: String,
        sourceRef: String
    ) {
        viewModelScope.launch {
            val card = existing?.copy(
                front = front.trim(),
                back = back.trim(),
                explanation = explanation.trim()
            ) ?: FlashcardItem(
                id = "fc_custom_${System.currentTimeMillis()}",
                deckMember = member.name,
                subject = subject.trim().ifBlank { "General Studies" },
                chapterId = "custom",
                front = front.trim(),
                back = back.trim(),
                explanation = explanation.trim(),
                difficulty = FlashcardDifficulty.MEDIUM.name,
                sourceRef = sourceRef.trim().ifBlank { "Personal Study Note" },
                masteryStatus = MasteryStatus.NEW.name,
                custom = true
            )
            cloudRepo.upsertFlashcard(card)
                .onSuccess { _statusBanner.value = "Flashcard saved to ${member.deckName}!" }
        }
    }

    // ==================== TEST RECORDING & NEGATIVE MARKING ====================

    fun recordQuizAttempt(
        testTitle: String,
        testType: String,
        chapterId: String,
        questions: List<ChapterQuestion>,
        selectedAnswers: Map<String, Int>,
        elapsedSeconds: Int
    ) {
        viewModelScope.launch {
            var correct = 0
            var wrong = 0
            var skipped = 0
            val weakTopics = mutableSetOf<String>()

            questions.forEach { q ->
                val chosen = selectedAnswers[q.questionId]
                when {
                    chosen == null -> skipped++
                    chosen == q.correctIndex -> correct++
                    else -> {
                        wrong++
                        weakTopics.add(q.relatedConcept)
                    }
                }
            }

            // Standard UPSC Prelims scoring: +2 for correct, -0.66 (-2/3) for wrong
            val netScore = (correct * 2.0) - (wrong * (2.0 / 3.0))
            val maxScore = questions.size * 2.0
            val attempted = correct + wrong
            val accuracy = if (attempted > 0) ((correct * 100.0) / attempted).toInt().coerceIn(0, 100) else 0

            val attempt = TestAttempt(
                id = "att_${System.currentTimeMillis()}",
                testTitle = testTitle,
                testType = testType,
                chapterId = chapterId,
                totalQuestions = questions.size,
                correctCount = correct,
                wrongCount = wrong,
                skippedCount = skipped,
                netScore = kotlin.math.round(netScore * 100.0) / 100.0,
                maxScore = maxScore,
                accuracyPercent = accuracy,
                timeTakenSeconds = elapsedSeconds.coerceAtLeast(1),
                weakTopics = weakTopics.take(20)
            )
            cloudRepo.recordTestAttempt(attempt)

            if (chapterId.isNotBlank()) {
                val chapter = NcertCatalogSeed.findChapter(chapterId)
                val existing = ((chapterProgressState.value as? UiState.Success)?.data ?: emptyList())
                    .firstOrNull { it.chapterId == chapterId }
                    ?: ChapterProgress(chapterId = chapterId, bookId = chapter?.bookId ?: "ncert_hist_6")
                cloudRepo.upsertChapterProgress(
                    existing.copy(
                        quizDone = true,
                        bestScorePercent = maxOf(existing.bestScorePercent, accuracy),
                        weakConcepts = (existing.weakConcepts + weakTopics).distinct().take(20)
                    )
                )
            }
            awardStars(2)
            showNextMotivation(slot = QuoteSlot.SESSION, preferredContext = "TEST_RESULT")
            _statusBanner.value = "Test saved! Score: ${attempt.netScore}/${attempt.maxScore} (${accuracy}% accuracy)"
        }
    }

    // ==================== ENGLISH ACADEMY & REMINDERS ====================

    fun logEnglishPractice(minutes: Int, vocabCount: Int, mistakes: Int, confidence: Int, notes: String) {
        viewModelScope.launch {
            cloudRepo.upsertEnglishLog(
                EnglishPracticeLog(
                    id = "eng_$todayDate",
                    dateStr = todayDate,
                    minutesPracticed = minutes.coerceIn(0, 300),
                    vocabLearnedCount = vocabCount.coerceIn(0, 500),
                    mistakesReviewed = mistakes.coerceIn(0, 200),
                    confidenceScore = confidence.coerceIn(1, 10),
                    notes = notes.trim()
                )
            ).onSuccess {
                awardStars(2)
                _statusBanner.value = "45-min English practice logged! +2 Purple Stars!"
            }
        }
    }

    fun updateReminder(context: Context, config: ReminderConfig, enabled: Boolean, hour: Int, minute: Int) {
        if (enabled && !StudyReminderScheduler.hasNotificationPermission(context)) {
            _statusBanner.value = "Please allow Notification permission first before enabling reminders."
            return
        }
        val updated = config.copy(enabled = enabled, hour = hour.coerceIn(0, 23), minute = minute.coerceIn(0, 59))
        viewModelScope.launch {
            cloudRepo.upsertReminderConfig(updated)
            if (updated.enabled) {
                StudyReminderScheduler.scheduleReminder(context, updated)
                _statusBanner.value = "${updated.title} scheduled for %02d:%02d".format(updated.hour, updated.minute)
            } else {
                StudyReminderScheduler.cancelReminder(context, updated.category, updated.title)
                _statusBanner.value = "${updated.title} paused."
            }
        }
    }

    fun sendTestNotification(context: Context) {
        if (!StudyReminderScheduler.hasNotificationPermission(context)) {
            _statusBanner.value = "Notification permission is not granted yet."
            return
        }
        StudyReminderScheduler.showNotification(
            context,
            "TEST_PING",
            "Purple Dream Study Bell",
            "Notifications are working! Your 9-hour UPSC 2029 timetable reminders are ready."
        )
    }

    fun syncNow() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            cloudRepo.updateProfileFields(mapOf("lastStudyDate" to todayDate))
                .onSuccess {
                    prefs.setLastSyncEpoch(now)
                    _statusBanner.value = "Cloud backup & sync verified!"
                }
                .onFailure {
                    _statusBanner.value = "Sync failed: ${it.localizedMessage}"
                }
        }
    }

    private suspend fun awardStars(delta: Int) {
        val prof = (profileState.value as? UiState.Success)?.data ?: return
        val nextStreak = if (prof.lastStudyDate == todayDate) prof.streakDays else prof.streakDays + 1
        cloudRepo.updateProfileFields(
            mapOf(
                "totalStars" to (prof.totalStars + delta),
                "streakDays" to nextStreak,
                "lastStudyDate" to todayDate
            )
        )
    }

    private fun <T> Flow<UiState<T>>.unwrapFirestoreErrors(fallbackMsg: String): Flow<UiState<T>> =
        catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val rootError = firestoreError ?: error
            emit(UiState.Error(rootError.message ?: fallbackMsg))
        }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
