package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.BtsMember
import com.example.data.model.ChapterProgress
import com.example.data.model.TaskStatus
import com.example.data.model.ThemeMode
import com.example.data.model.UiState
import com.example.data.model.UserProfile
import com.example.data.seed.NcertCatalogSeed
import com.example.data.seed.QuoteSelectionEngine
import com.example.ui.theme.BabyPink
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.LavenderPurple
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.theme.SoftLilac
import com.example.ui.viewmodel.CivilsViewModel

@Composable
fun DashboardScreen(
    viewModel: CivilsViewModel,
    onNavigateToSyllabus: () -> Unit,
    onOpenChapter: (String) -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToMotivation: () -> Unit,
    onNavigateToTests: () -> Unit,
    onNavigateToAiTeacher: () -> Unit,
    onNavigateToProgress: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val tasksState by viewModel.studyTasksState.collectAsStateWithLifecycle()
    val chaptersState by viewModel.chapterProgressState.collectAsStateWithLifecycle()
    val activeMotivation by viewModel.activeMotivation.collectAsStateWithLifecycle()
    val isGeneratingQuotes by viewModel.isGeneratingQuotes.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val activeTimerId by viewModel.activeTimerTaskId.collectAsStateWithLifecycle()
    val activeSeconds by viewModel.activeTimerElapsedSeconds.collectAsStateWithLifecycle()

    val profile = (profileState as? UiState.Success)?.data ?: UserProfile()
    val todayTasks = ((tasksState as? UiState.Success)?.data ?: emptyList())
        .filter { it.taskDate == viewModel.todayDate }
    val completedMinutes = todayTasks.sumOf {
        if (it.status == TaskStatus.COMPLETED.name) it.actualMinutes else 0
    }
    val targetMinutes = profile.dailyStudyTargetMinutes.coerceAtLeast(540)
    val dayProgress = (completedMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f)

    val chapterProgressList = (chaptersState as? UiState.Success)?.data ?: emptyList()
    val dueRevisions = chapterProgressList.filter {
        it.completed && it.nextRevisionEpochMs in 1..System.currentTimeMillis()
    }

    val memberOfDay = QuoteSelectionEngine.memberOfTheDay(viewModel.todayDate)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Purple Dream Banner + Streak Counter
        item {
            PurpleDreamCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 26.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_purple_galaxy_hero),
                        contentDescription = "Purple Dream study hero artwork",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(172.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(172.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        DeepPurple.copy(alpha = 0.25f),
                                        DeepPurple.copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = BabyPink.copy(alpha = 0.92f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Purple Streak",
                                        tint = DeepPurple,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "${profile.streakDays}d Streak • ${profile.totalStars}★",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = DeepPurple,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            IconButton(
                                onClick = { viewModel.toggleThemeMode() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f))
                                    .testTag("theme_toggle_button")
                            ) {
                                Icon(
                                    imageVector = if (themeMode == ThemeMode.DARK_GALAXY) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Switch Light Lavender / Dark Galaxy theme",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "Welcome, ${profile.displayName}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Text(
                            text = "Goal: IAS ${profile.targetAttemptYear} • Finish Syllabus by ${profile.syllabusTargetDate} • Optional: ${profile.optionalSubject}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftLilac
                        )
                    }
                }
            }
        }

        // 2. Non-Repeating BTS Motivation Corner Card
        item {
            val banner = activeMotivation
            val displayMember = banner?.quote?.member ?: memberOfDay
            PurpleDreamCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = displayMember.accentColor,
                                shape = CircleShape,
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(displayMember.emblemSymbol)
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Member of the Day: ${memberOfDay.displayName} • Active: ${displayMember.displayName}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = banner?.quote?.labelType?.badgeText ?: "Original Motivation",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Row {
                            IconButton(
                                onClick = { viewModel.toggleCurrentMotivationFavourite() },
                                modifier = Modifier.testTag("fav_quote_button")
                            ) {
                                Icon(
                                    imageVector = if (banner?.isFavourite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Save favourite quote",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.showNextMotivation() },
                                modifier = Modifier.testTag("shuffle_quote_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shuffle,
                                    contentDescription = "Shuffle non-repeating quote",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Text(
                        text = banner?.quote?.text ?: "Preparing your fresh non-repeating daily motivation...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = banner?.quote?.sourceNote ?: "Inspired by ${memberOfDay.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (banner?.collectionExhausted == true) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "All ${viewModel.quotePool.collectAsStateWithLifecycle().value.size} quotes seen!",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = { viewModel.generateNewMotivationBatchWithAi() },
                                    enabled = !isGeneratingQuotes,
                                    modifier = Modifier.testTag("generate_quote_batch_button")
                                ) {
                                    if (isGeneratingQuotes) {
                                        CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                                    } else {
                                        Text("Fresh AI Batch")
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${banner?.remainingUnseenCount ?: 140} unseen messages left",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = onNavigateToMotivation,
                            modifier = Modifier.testTag("open_motivation_corner_button")
                        ) {
                            Text("All 7 Members & History")
                        }
                    }
                }
            }
        }

        // 3. Today's 9-Hour Study Plan Summary
        item {
            PurpleDreamCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToPlanner)
                    .testTag("planner_summary_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Study Plan", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("Today's 9-Hour Study Target", style = MaterialTheme.typography.titleMedium)
                        }
                        Text(
                            text = "${completedMinutes / 60}h ${completedMinutes % 60}m / 9h",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    LinearProgressIndicator(
                        progress = { dayProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50)),
                        color = LavenderPurple,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    if (activeTimerId != null) {
                        Text(
                            text = "⏱️ Active study timer running: ${activeSeconds / 60}m ${activeSeconds % 60}s",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "${todayTasks.count { it.status == TaskStatus.COMPLETED.name }} of ${todayTasks.size} blocks finished • Breaks tracked separately",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 4. Milestone Chapter Quick-Start Card
        item {
            PurpleDreamCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChapter("ncert_hist_6_ch1") }
                    .testTag("milestone_chapter_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_history_academy_banner),
                        contentDescription = "NCERT History Class 6 Chapter 1",
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "MILESTONE CHAPTER READY (22 SECTIONS + 70 MCQs)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Class 6 History Ch 1: What, Where, How and When?",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "क्या, कब, कहाँ और कैसे? • Hindi Notes + Memory Tricks + PYQs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 5. Due Spaced Revisions (1d, 3d, 7d, 15d, 30d)
        if (dueRevisions.isNotEmpty()) {
            item {
                PurpleDreamCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Refresh, contentDescription = "Revision Due", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("Spaced Repetition Due Today (${dueRevisions.size})", style = MaterialTheme.typography.titleMedium)
                        }
                        dueRevisions.forEach { prog ->
                            val chap = NcertCatalogSeed.findChapter(prog.chapterId)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = chap?.title ?: prog.chapterId,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Button(onClick = { viewModel.advanceSpacedRevision(prog) }) {
                                    Text("Revise")
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Academy Quick-Launch Grid
        item {
            Text(
                text = "Personal Coaching Academies",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionTile(
                        title = "Syllabus Library",
                        subtitle = "${NcertCatalogSeed.totalChaptersCount()} chapters",
                        icon = Icons.Default.Book,
                        onClick = onNavigateToSyllabus,
                        modifier = Modifier.weight(1f).testTag("tile_syllabus")
                    )
                    QuickActionTile(
                        title = "BTS Flashcards",
                        subtitle = "7 Member Decks",
                        icon = Icons.Default.Style,
                        onClick = onNavigateToFlashcards,
                        modifier = Modifier.weight(1f).testTag("tile_flashcards")
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionTile(
                        title = "AI Teacher",
                        subtitle = "Hindi + English",
                        icon = Icons.Default.Psychology,
                        onClick = onNavigateToAiTeacher,
                        modifier = Modifier.weight(1f).testTag("tile_ai_teacher")
                    )
                    QuickActionTile(
                        title = "Tests & Zones",
                        subtitle = "Prelims/Mains/Eng",
                        icon = Icons.Default.Quiz,
                        onClick = onNavigateToTests,
                        modifier = Modifier.weight(1f).testTag("tile_tests")
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionTile(
                        title = "9h Plan & Bells",
                        subtitle = "11 Reminders",
                        icon = Icons.Default.NotificationsActive,
                        onClick = onNavigateToReminders,
                        modifier = Modifier.weight(1f).testTag("tile_reminders")
                    )
                    QuickActionTile(
                        title = "My Progress",
                        subtitle = "Reports & Sync",
                        icon = Icons.Default.Timeline,
                        onClick = onNavigateToProgress,
                        modifier = Modifier.weight(1f).testTag("tile_progress")
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PurpleDreamCard(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    }
}
