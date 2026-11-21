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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContentStatus
import com.example.data.model.SyllabusSection
import com.example.data.model.UiState
import com.example.data.seed.NcertCatalogSeed
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintContainer
import com.example.ui.viewmodel.CivilsViewModel

@Composable
fun SyllabusLibraryScreen(
    viewModel: CivilsViewModel,
    onOpenChapter: (String) -> Unit
) {
    val progressState by viewModel.chapterProgressState.collectAsStateWithLifecycle()
    val offlineIds by viewModel.offlineChapterIds.collectAsStateWithLifecycle()

    var selectedSection by remember { mutableStateOf<SyllabusSection?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val progressMap = remember(progressState) {
        ((progressState as? UiState.Success)?.data ?: emptyList()).associateBy { it.chapterId }
    }

    val filteredBooks = remember(selectedSection, searchQuery) {
        NcertCatalogSeed.allBooks.filter { book ->
            val sectionMatch = selectedSection == null || book.section == selectedSection
            val queryMatch = searchQuery.isBlank() ||
                book.title.contains(searchQuery, ignoreCase = true) ||
                book.subject.contains(searchQuery, ignoreCase = true) ||
                book.chapters.any {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                        it.titleHi.contains(searchQuery, ignoreCase = true) ||
                        it.keyFocus.contains(searchQuery, ignoreCase = true)
                }
            sectionMatch && queryMatch
        }
    }

    val totalChapters = NcertCatalogSeed.totalChaptersCount()
    val completedChapters = progressMap.values.count { it.completed }
    val fullReadyCount = NcertCatalogSeed.fullContentChaptersCount()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Complete UPSC Syllabus Library", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "$completedChapters / $totalChapters chapters completed • $fullReadyCount chapter(s) with full 22-section notes ready (remaining syllabus mapped transparently)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LinearProgressIndicator(
                        progress = { if (totalChapters > 0) completedChapters.toFloat() / totalChapters else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().testTag("syllabus_search_input"),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search syllabus") },
                label = { Text("Search book, chapter, or topic (e.g. Narmada, Polity, Optional)") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedSection == null,
                    onClick = { selectedSection = null },
                    label = { Text("All Sections") }
                )
                SyllabusSection.entries.forEach { sec ->
                    FilterChip(
                        selected = selectedSection == sec,
                        onClick = { selectedSection = sec },
                        label = { Text(sec.label.substringBefore(" (")) }
                    )
                }
            }
        }

        items(filteredBooks, key = { it.bookId }) { book ->
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${book.classOrPaper} • ${book.subject}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(book.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "${book.titleHi} • ${book.sourceInfo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        val bookDone = book.chapters.count { progressMap[it.chapterId]?.completed == true }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "$bookDone/${book.chapters.size}",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    book.chapters.forEach { chapter ->
                        val prog = progressMap[chapter.chapterId]
                        val isCompleted = prog?.completed == true && chapter.contentStatus == ContentStatus.FULL_CONTENT
                        val isOffline = chapter.chapterId in offlineIds
                        val isFull = chapter.contentStatus == ContentStatus.FULL_CONTENT

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenChapter(chapter.chapterId) }
                                .testTag("chapter_row_${chapter.chapterId}"),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when {
                                        isCompleted -> Icons.Default.CheckCircle
                                        isFull -> Icons.Default.MenuBook
                                        else -> Icons.Default.Lock
                                    },
                                    contentDescription = "Chapter status",
                                    tint = when {
                                        isCompleted -> SuccessMint
                                        isFull -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ch ${chapter.chapterNumber}: ${chapter.title}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${chapter.titleHi} [${chapter.chapterId}]",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Surface(
                                        color = if (isFull) SuccessMintContainer else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = if (isFull) "Full Notes + Memory + 70 Qs Ready" else "Syllabus Mapped • Notes Pending",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isFull) SuccessMint else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                if (isFull) {
                                    IconButton(
                                        onClick = { viewModel.toggleOfflineDownload(chapter.chapterId) },
                                        modifier = Modifier.testTag("offline_toggle_${chapter.chapterId}")
                                    ) {
                                        Icon(
                                            imageVector = if (isOffline) Icons.Default.CloudDone else Icons.Default.CloudDownload,
                                            contentDescription = "Download chapter offline",
                                            tint = if (isOffline) SuccessMint else MaterialTheme.colorScheme.primary
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
}
