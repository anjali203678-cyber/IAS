package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.firebase.AuthManager
import com.example.data.model.MasteryStatus
import com.example.data.model.TaskStatus
import com.example.data.model.ThemeMode
import com.example.data.model.UiState
import com.example.data.model.UserProfile
import com.example.data.seed.NcertCatalogSeed
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.viewmodel.CivilsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressDashboardScreen(
    viewModel: CivilsViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val chaptersState by viewModel.chapterProgressState.collectAsStateWithLifecycle()
    val tasksState by viewModel.studyTasksState.collectAsStateWithLifecycle()
    val cardsState by viewModel.flashcardsState.collectAsStateWithLifecycle()
    val testsState by viewModel.testAttemptsState.collectAsStateWithLifecycle()
    val englishState by viewModel.englishLogsState.collectAsStateWithLifecycle()
    val lastSync by viewModel.lastSyncEpoch.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    val profile = (profileState as? UiState.Success)?.data ?: UserProfile()
    val chapterProgress = (chaptersState as? UiState.Success)?.data ?: emptyList()
    val tasks = (tasksState as? UiState.Success)?.data ?: emptyList()
    val cards = (cardsState as? UiState.Success)?.data ?: emptyList()
    val tests = (testsState as? UiState.Success)?.data ?: emptyList()
    val englishLogs = (englishState as? UiState.Success)?.data ?: emptyList()

    val totalChapters = NcertCatalogSeed.totalChaptersCount()
    val completedChapters = chapterProgress.count { it.completed }
    val todayCompletedMin = tasks.filter { it.taskDate == viewModel.todayDate && it.status == TaskStatus.COMPLETED.name }
        .sumOf { it.actualMinutes }
    val masteredCards = cards.count { it.masteryStatus == MasteryStatus.KNOWN.name }
    val avgAccuracy = if (tests.isNotEmpty()) tests.map { it.accuracyPercent }.average().toInt() else 0
    val weakTopics = tests.flatMap { it.weakTopics }.distinct().take(6)

    val syncText = remember(lastSync) {
        if (lastSync <= 0L) "Sync pending"
        else SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(lastSync))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timeline, contentDescription = "Progress", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("UPSC CSE 2029 Progress Report", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        text = "${profile.displayName} • Target Attempt: ${profile.targetAttemptYear} • Syllabus Finish: ${profile.syllabusTargetDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Streak: ${profile.streakDays} days • Purple Stars: ${profile.totalStars}★ • Favourite: ${profile.favouriteMember}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            MetricProgressCard(
                title = "Syllabus Completion",
                valueText = "$completedChapters / $totalChapters chapters",
                progress = if (totalChapters > 0) completedChapters.toFloat() / totalChapters else 0f
            )
        }
        item {
            MetricProgressCard(
                title = "Today's 9-Hour Study Progress",
                valueText = "${todayCompletedMin / 60}h ${todayCompletedMin % 60}m / 9h 00m",
                progress = (todayCompletedMin / 540f).coerceIn(0f, 1f)
            )
        }
        item {
            MetricProgressCard(
                title = "Flashcard Retention Mastery",
                valueText = "$masteredCards / ${cards.size} cards marked Known",
                progress = if (cards.isNotEmpty()) masteredCards.toFloat() / cards.size else 0f
            )
        }

        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Tests & English Practice", style = MaterialTheme.typography.titleMedium)
                    Text("Mock / Chapter Tests Taken: ${tests.size} • Average Accuracy: $avgAccuracy%", style = MaterialTheme.typography.bodySmall)
                    Text("45-Min English Sessions Logged: ${englishLogs.size}", style = MaterialTheme.typography.bodySmall)
                    if (weakTopics.isNotEmpty()) {
                        Text(
                            text = "Priority Revision Topics: ${weakTopics.joinToString(", ")}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Appearance & Cloud Sync", style = MaterialTheme.typography.titleMedium)
                    Text("Last Firestore Backup: $syncText", style = MaterialTheme.typography.bodySmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.syncNow() },
                            modifier = Modifier.weight(1f).testTag("sync_now_button")
                        ) {
                            Icon(Icons.Default.CloudSync, contentDescription = "Sync now")
                            Spacer(Modifier.width(6.dp))
                            Text("Sync Now")
                        }
                        OutlinedButton(
                            onClick = { viewModel.toggleThemeMode() },
                            modifier = Modifier.weight(1f).testTag("progress_theme_button")
                        ) {
                            Icon(Icons.Default.DarkMode, contentDescription = "Toggle theme")
                            Spacer(Modifier.width(6.dp))
                            Text(if (themeMode == ThemeMode.DARK_GALAXY) "Lavender" else "Dark Galaxy")
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            AuthManager.signOut(credentialManager, scope)
                        },
                        modifier = Modifier.fillMaxWidth().testTag("sign_out_button")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Sign out")
                        Spacer(Modifier.width(8.dp))
                        Text("Sign Out")
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricProgressCard(title: String, valueText: String, progress: Float) {
    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(valueText, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(9.dp).clip(RoundedCornerShape(50))
            )
        }
    }
}
