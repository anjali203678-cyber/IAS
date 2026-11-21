package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ChapterQuestion
import com.example.data.model.ContentStatus
import com.example.data.model.DetailedChapterContent
import com.example.data.model.QuestionCategory
import com.example.data.model.UiState
import com.example.data.seed.HistoryCh1NotesSeed
import com.example.data.seed.NcertCatalogSeed
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.ErrorRoseContainer
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintContainer
import com.example.ui.viewmodel.CivilsViewModel
import kotlinx.coroutines.delay

@Composable
fun ChapterDetailScreen(
    chapterId: String,
    viewModel: CivilsViewModel,
    onBack: () -> Unit,
    onAskAiTeacher: (String) -> Unit
) {
    val chapter = remember(chapterId) { NcertCatalogSeed.findChapter(chapterId) }
    val book = remember(chapter) { chapter?.let { NcertCatalogSeed.findBook(it.bookId) } }
    val progressState by viewModel.chapterProgressState.collectAsStateWithLifecycle()
    val offlineIds by viewModel.offlineChapterIds.collectAsStateWithLifecycle()

    val chapterProgress = remember(progressState, chapterId) {
        ((progressState as? UiState.Success)?.data ?: emptyList()).firstOrNull { it.chapterId == chapterId }
    }
    val isOffline = chapterId in offlineIds

    if (chapter == null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chapter not found: $chapterId", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack) { Text("Go Back") }
        }
        return
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val isFull = chapter.contentStatus == ContentStatus.FULL_CONTENT
    val detailedContent = remember(chapterId) {
        if (chapterId == "ncert_hist_6_ch1") HistoryCh1NotesSeed.chapterContent else null
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header Bar
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("chapter_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to syllabus")
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${book?.classOrPaper ?: ""} • Ch ${chapter.chapterNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                    Text(
                        text = chapter.titleHi,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                if (isFull) {
                    IconButton(
                        onClick = { viewModel.toggleOfflineDownload(chapter.chapterId) },
                        modifier = Modifier.testTag("chapter_offline_button")
                    ) {
                        Icon(
                            imageVector = if (isOffline) Icons.Default.CloudDone else Icons.Default.CloudDownload,
                            contentDescription = "Save chapter offline",
                            tint = if (isOffline) SuccessMint else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        if (!isFull || detailedContent == null) {
            OutlineOnlyChapterView(
                chapterTitle = chapter.title,
                chapterTitleHi = chapter.titleHi,
                keyFocus = chapter.keyFocus,
                bookTitle = book?.title ?: "UPSC Syllabus",
                onAskAiTeacher = {
                    onAskAiTeacher("Teach me '${chapter.title}' (${book?.title ?: "UPSC"}) from absolute basics in simple Hindi with important English terms and a memory trick.")
                }
            )
            return
        }

        val tabs = listOf("Notes (22)", "Memory & Map", "70 Qs & PYQs")
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, maxLines = 1) },
                    modifier = Modifier.testTag("chapter_tab_$index")
                )
            }
        }

        when (selectedTab) {
            0 -> DetailedNotesTab(
                content = detailedContent,
                notesRead = chapterProgress?.notesRead == true,
                isCompleted = chapterProgress?.completed == true,
                onMarkNotesRead = { viewModel.markChapterSectionProgress(chapterId, notesRead = true) },
                onMarkCompleted = { viewModel.markChapterSectionProgress(chapterId, notesRead = true, markComplete = true) },
                onAskAiTeacher = {
                    onAskAiTeacher("Explain '${detailedContent.chapterTitleEn}' key doubts in simple Hindi and test me orally.")
                }
            )
            1 -> MemoryAcademyTab(
                content = detailedContent,
                memoryDone = chapterProgress?.memoryDone == true,
                onMarkMemoryDone = { viewModel.markChapterSectionProgress(chapterId, memoryDone = true) },
                onGenerateSuggestedFlashcards = { viewModel.generateSuggestedFlashcardsForChapter(chapterId) }
            )
            else -> QuestionBankTab(
                chapterId = chapterId,
                questions = detailedContent.questions,
                pyqNote = detailedContent.pyqAvailabilityNote,
                onSubmitAttempt = { title, qs, answers, secs ->
                    viewModel.recordQuizAttempt(
                        testTitle = title,
                        testType = "CHAPTER_TEST",
                        chapterId = chapterId,
                        questions = qs,
                        selectedAnswers = answers,
                        elapsedSeconds = secs
                    )
                }
            )
        }
    }
}

@Composable
private fun OutlineOnlyChapterView(
    chapterTitle: String,
    chapterTitleHi: String,
    keyFocus: String,
    bookTitle: String,
    onAskAiTeacher: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = "Pending notes", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Syllabus Mapped • Detailed Notes Pending", style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        text = "$bookTitle\nChapter: $chapterTitle ($chapterTitleHi)",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Key UPSC Focus Topics: $keyFocus",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Honest Progress Rule: This chapter cannot be marked as completed until its full 22-section study notes and question bank are authored. Meanwhile, you can learn this chapter interactively with your Gemini AI Teacher right now!",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Button(
                        onClick = onAskAiTeacher,
                        modifier = Modifier.fillMaxWidth().testTag("ask_ai_for_outline_chapter")
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = "AI Teacher")
                        Spacer(Modifier.width(8.dp))
                        Text("Learn This Chapter Live with AI Teacher (Hindi)")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailedNotesTab(
    content: DetailedChapterContent,
    notesRead: Boolean,
    isCompleted: Boolean,
    onMarkNotesRead: () -> Unit,
    onMarkCompleted: () -> Unit,
    onAskAiTeacher: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Sections 1 & 2: Introduction + Beginner Hindi Explanation
        item {
            SectionCard(title = "1 & 2. अध्याय परिचय व सरल हिंदी व्याख्या (Introduction & Basics)") {
                Text(content.introHi, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = content.beginnerExplanationHi,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Section 3: Gradual English Terminology
        item {
            SectionCard(title = "3. ज़रूरी English UPSC शब्दावली (Gradual English Terms)") {
                content.englishTerminology.forEach { term ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${term.englishTerm} — ${term.hindiMeaning}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(term.simpleExplanationHi, style = MaterialTheme.typography.bodySmall)
                            Text("UPSC Tip: ${term.examUsage}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Section 4: Topic-wise Notes
        item {
            SectionCard(title = "4. विस्तृत विषय-वार नोट्स (Detailed Topic Notes)") {
                content.topicNotes.forEach { block ->
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Text("${block.headingHi} (${block.headingEn})", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(4.dp))
                        Text(block.explanationHi, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(4.dp))
                        block.keyBullets.forEach { bullet ->
                            Text("• $bullet", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp, top = 2.dp))
                        }
                    }
                }
            }
        }

        // Sections 5 & 6: Definitions + Key Facts
        item {
            SectionCard(title = "5 & 6. मुख्य परिभाषाएँ, तथ्य और व्यक्तित्व") {
                content.definitions.forEach { (term, def) ->
                    Text("• $term: $def", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                }
                Spacer(Modifier.height(8.dp))
                content.keyFactsAndPersonalities.forEach { row ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("${row.label} (${row.yearOrPlace})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                            Text(row.detailHi, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        // Section 7: Comparison Tables
        item {
            SectionCard(title = "7. तुलनात्मक तालिकाएँ (Comparison Tables)") {
                content.comparisonTables.forEach { table ->
                    Text(table.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 6.dp, bottom = 4.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(table.headers.joinToString(" | "), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                            table.rows.forEach { r ->
                                Text(r.joinToString(" | "), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        // Section 8: Timeline & Map Locations
        item {
            SectionCard(title = "8. कालक्रम (Timeline) एवं मानचित्र स्थल (Map Locations)") {
                content.timeline.forEach { entry ->
                    Text("⏳ ${entry.period}: ${entry.eventHi} (${entry.eventEn})", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 3.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text("Map Locations:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                content.mapLocations.forEach { (loc, desc) ->
                    Text("📍 $loc — $desc", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }

        // Sections 9, 10, 11, 12: Prelims, Mains, Case Studies, Exam Points
        item {
            SectionCard(title = "9–12. Prelims Pointers, Mains Analysis & Case Studies") {
                Text("Prelims Focus:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                content.prelimsPointers.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(8.dp))
                Text("Mains Analytical Points:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                content.mainsAnalyticalPoints.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(8.dp))
                Text("Examples & Case Studies:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                content.examplesAndCaseStudies.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(8.dp))
                Text("High-Yield Exam Points:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                content.importantExamPoints.forEach { Text("★ $it", style = MaterialTheme.typography.bodySmall) }
            }
        }

        // Section 13: Commonly Confused Concepts
        item {
            SectionCard(title = "13. सामान्य भ्रम एवं स्पष्टता (Commonly Confused Concepts)") {
                content.confusedConcepts.forEach { item ->
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(item.pairTitle, style = MaterialTheme.typography.titleSmall)
                            Text("A: ${item.sideA}", style = MaterialTheme.typography.bodySmall)
                            Text("B: ${item.sideB}", style = MaterialTheme.typography.bodySmall)
                            Text("Memory Tip: ${item.memoryTipHi}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Sections 14, 15 & 22: Summary, 1-Page Revision Sheet & Sources
        item {
            SectionCard(title = "14, 15 & 22. सारांश, One-Page Revision Sheet एवं स्रोत") {
                Text(content.chapterSummaryHi, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("One-Page Rapid Revision:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                content.onePageRevisionSheet.forEach { Text("✔ $it", style = MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(8.dp))
                Text("Authentic Sources:", style = MaterialTheme.typography.titleSmall)
                content.sourceReferences.forEach { Text("📚 $it", style = MaterialTheme.typography.labelSmall) }
            }
        }

        // Completion Actions
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = onMarkNotesRead,
                            modifier = Modifier.weight(1f).testTag("mark_notes_read_button")
                        ) {
                            Text(if (notesRead) "Notes Read ✔" else "Mark Notes Read")
                        }
                        Button(
                            onClick = onMarkCompleted,
                            enabled = !isCompleted,
                            modifier = Modifier.weight(1f).testTag("mark_chapter_complete_button")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Complete")
                            Spacer(Modifier.width(6.dp))
                            Text(if (isCompleted) "Completed ✔" else "Complete (+5★)")
                        }
                    }
                    OutlinedButton(
                        onClick = onAskAiTeacher,
                        modifier = Modifier.fillMaxWidth().testTag("ask_ai_chapter_button")
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = "AI Teacher")
                        Spacer(Modifier.width(8.dp))
                        Text("Ask AI Teacher for Oral Revision / Doubts")
                    }
                }
            }
        }
    }
}

@Composable
private fun MemoryAcademyTab(
    content: DetailedChapterContent,
    memoryDone: Boolean,
    onMarkMemoryDone: () -> Unit,
    onGenerateSuggestedFlashcards: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionCard(title = "16. Memory Tricks, Mnemonics & Stories") {
                content.memoryTechniques.forEach { tech ->
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("${tech.techniqueType.uppercase()} • ${tech.title}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                            Text(tech.contentHi, style = MaterialTheme.typography.bodyMedium)
                            Text("Visual Cue: ${tech.visualCue}", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        item {
            SectionCard(title = "17. Visual Mind Map (${content.mindMapRoot})") {
                content.mindMapBranches.forEach { branch ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🌿 ${branch.branchTitle}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                            branch.children.forEach { child ->
                                Text("   ↳ $child", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        item {
            SectionCard(title = "18. Chapter Flashcards & Actions") {
                Text("This chapter includes ${content.starterFlashcards.size} curated bilingual flashcards ready for your BTS spaced-repetition decks.", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onGenerateSuggestedFlashcards,
                        modifier = Modifier.weight(1f).testTag("auto_suggest_flashcards_button")
                    ) {
                        Icon(Icons.Default.Style, contentDescription = "Flashcards")
                        Spacer(Modifier.width(6.dp))
                        Text("Add to Decks")
                    }
                    OutlinedButton(
                        onClick = onMarkMemoryDone,
                        modifier = Modifier.weight(1f).testTag("mark_memory_done_button")
                    ) {
                        Text(if (memoryDone) "Memory Done ✔" else "Mark Revised")
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionBankTab(
    chapterId: String,
    questions: List<ChapterQuestion>,
    pyqNote: String,
    onSubmitAttempt: (String, List<ChapterQuestion>, Map<String, Int>, Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<QuestionCategory?>(null) }
    val selectedAnswers = remember(chapterId) { mutableStateMapOf<String, Int>() }
    var submitted by remember(chapterId) { mutableStateOf(false) }
    var elapsedSeconds by remember(chapterId) { mutableIntStateOf(0) }

    LaunchedEffect(chapterId, submitted) {
        while (!submitted) {
            delay(1000L)
            elapsedSeconds++
        }
    }

    val filteredQuestions = remember(questions, selectedCategory) {
        if (selectedCategory == null) questions else questions.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("70-Question Bank (30 Basic + 20 UPSC + 10 Stmt + 10 PYQs)", style = MaterialTheme.typography.titleSmall)
                        Text("${elapsedSeconds / 60}m ${elapsedSeconds % 60}s", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(pyqNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All (${questions.size})") }
                        )
                        QuestionCategory.entries.forEach { cat ->
                            val cnt = questions.count { it.category == cat }
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text("${cat.label.substringBefore(" MCQ")} ($cnt)") }
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                submitted = true
                                onSubmitAttempt(
                                    "Ch 1 ${selectedCategory?.label ?: "Full 70-Q Bank"}",
                                    filteredQuestions,
                                    selectedAnswers.toMap(),
                                    elapsedSeconds
                                )
                            },
                            enabled = selectedAnswers.isNotEmpty() && !submitted,
                            modifier = Modifier.weight(1f).testTag("submit_chapter_quiz_button")
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = "Submit Quiz")
                            Spacer(Modifier.width(6.dp))
                            Text(if (submitted) "Attempt Saved ✔" else "Submit & Score (+2/-0.66)")
                        }
                        if (submitted) {
                            OutlinedButton(
                                onClick = {
                                    selectedAnswers.clear()
                                    elapsedSeconds = 0
                                    submitted = false
                                }
                            ) {
                                Text("Retake")
                            }
                        }
                    }
                }
            }
        }

        items(filteredQuestions, key = { it.questionId }) { q ->
            val chosen = selectedAnswers[q.questionId]
            val showReveal = submitted || chosen != null
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (q.pyqYear != null) "${q.sourceRef} (${q.pyqYear})" else q.category.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(q.relatedConcept, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(q.questionText, style = MaterialTheme.typography.titleSmall)
                    Text(q.questionTextHi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    q.options.forEachIndexed { idx, opt ->
                        val isSelected = chosen == idx
                        val isCorrect = idx == q.correctIndex
                        val bg = when {
                            showReveal && isCorrect -> SuccessMintContainer
                            showReveal && isSelected && !isCorrect -> ErrorRoseContainer
                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        }
                        Surface(
                            color = bg,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !submitted) { selectedAnswers[q.questionId] = idx }
                        ) {
                            Text(
                                text = "${('A' + idx)}. $opt",
                                style = MaterialTheme.typography.bodyMedium,
                                color = when {
                                    showReveal && isCorrect -> SuccessMint
                                    showReveal && isSelected && !isCorrect -> ErrorRose
                                    else -> MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    if (showReveal) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Explanation (व्याख्या): ${q.explanationHi}", style = MaterialTheme.typography.bodySmall)
                                if (q.incorrectOptionsReason.isNotEmpty()) {
                                    Text(
                                        text = "Why others are wrong: ${q.incorrectOptionsReason.joinToString(" • ")}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.School, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            content()
        }
    }
}
