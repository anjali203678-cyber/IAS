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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.QuestionCategory
import com.example.data.model.UiState
import com.example.data.seed.HistoryCh1QuestionsSeed
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.ErrorRoseContainer
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.theme.SuccessMint
import com.example.ui.theme.SuccessMintContainer
import com.example.ui.viewmodel.AiTeacherViewModel
import com.example.ui.viewmodel.CivilsViewModel
import kotlinx.coroutines.delay

@Composable
fun TestAcademyScreen(
    viewModel: CivilsViewModel,
    aiViewModel: AiTeacherViewModel
) {
    var topSection by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = topSection) {
            Tab(
                selected = topSection == 0,
                onClick = { topSection = 0 },
                text = { Text("Prelims Test Zone") },
                modifier = Modifier.testTag("test_zone_prelims_tab")
            )
            Tab(
                selected = topSection == 1,
                onClick = { topSection = 1 },
                text = { Text("Mains, CA & English") },
                modifier = Modifier.testTag("test_zone_academies_tab")
            )
        }

        if (topSection == 0) {
            PrelimsZoneTab(viewModel = viewModel)
        } else {
            SpecializedAcademiesScreen(viewModel = viewModel, aiViewModel = aiViewModel)
        }
    }
}

@Composable
private fun PrelimsZoneTab(viewModel: CivilsViewModel) {
    val attemptsState by viewModel.testAttemptsState.collectAsStateWithLifecycle()
    val pastAttempts = (attemptsState as? UiState.Success)?.data ?: emptyList()

    var activeQuizMode by remember { mutableStateOf<String?>(null) } // null, DAILY_10, PYQ_10, STATEMENT_10
    val selectedAnswers = remember(activeQuizMode) { mutableStateMapOf<String, Int>() }
    var submitted by remember(activeQuizMode) { mutableStateOf(false) }
    var timerSeconds by remember(activeQuizMode) { mutableIntStateOf(0) }

    // Negative Marking Simulator Inputs
    var calcCorrect by remember { mutableStateOf("45") }
    var calcWrong by remember { mutableStateOf("18") }
    var calcSkipped by remember { mutableStateOf("37") }

    LaunchedEffect(activeQuizMode, submitted) {
        if (activeQuizMode != null && !submitted) {
            while (!submitted) {
                delay(1000L)
                timerSeconds++
            }
        }
    }

    val quizQuestions: List<ChapterQuestion> = remember(activeQuizMode) {
        when (activeQuizMode) {
            "DAILY_10" -> HistoryCh1QuestionsSeed.allQuestions.take(10)
            "PYQ_10" -> HistoryCh1QuestionsSeed.allQuestions.filter { it.category == QuestionCategory.AUTHENTIC_PYQ }
            "STATEMENT_10" -> HistoryCh1QuestionsSeed.allQuestions.filter { it.category == QuestionCategory.STATEMENT_MCQ }
            else -> emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Negative Marking Calculator & Strategy Guide
        item {
            val c = calcCorrect.toIntOrNull() ?: 0
            val w = calcWrong.toIntOrNull() ?: 0
            val s = calcSkipped.toIntOrNull() ?: 0
            val net = (c * 2.0) - (w * (2.0 / 3.0))
            val totalQ = (c + w + s).coerceAtLeast(1)
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = "Negative Marking Calculator", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("UPSC Prelims Negative Marking (+2 / -0.66)", style = MaterialTheme.typography.titleMedium)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = calcCorrect,
                            onValueChange = { calcCorrect = it.filter(Char::isDigit).take(3) },
                            label = { Text("Right") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = calcWrong,
                            onValueChange = { calcWrong = it.filter(Char::isDigit).take(3) },
                            label = { Text("Wrong") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = calcSkipped,
                            onValueChange = { calcSkipped = it.filter(Char::isDigit).take(3) },
                            label = { Text("Skipped") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    Text(
                        text = "Net Score: %.2f / %d  (Penalty: -%.2f marks)".format(net, totalQ * 2, w * (2.0 / 3.0)),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Mock Quiz Launcher
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Quiz, contentDescription = "Prelims Mocks", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Launch Timed Prelims Practice", style = MaterialTheme.typography.titleMedium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = activeQuizMode == "DAILY_10",
                            onClick = {
                                activeQuizMode = "DAILY_10"
                                submitted = false
                                timerSeconds = 0
                            },
                            label = { Text("Daily 10 Basics") }
                        )
                        FilterChip(
                            selected = activeQuizMode == "PYQ_10",
                            onClick = {
                                activeQuizMode = "PYQ_10"
                                submitted = false
                                timerSeconds = 0
                            },
                            label = { Text("10 Authentic PYQs") }
                        )
                        FilterChip(
                            selected = activeQuizMode == "STATEMENT_10",
                            onClick = {
                                activeQuizMode = "STATEMENT_10"
                                submitted = false
                                timerSeconds = 0
                            },
                            label = { Text("10 Statement MCQs") }
                        )
                    }
                }
            }
        }

        if (activeQuizMode != null && quizQuestions.isNotEmpty()) {
            item {
                PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, contentDescription = "Timer", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(6.dp))
                            Text("%02d:%02d".format(timerSeconds / 60, timerSeconds % 60), style = MaterialTheme.typography.titleMedium)
                        }
                        Button(
                            onClick = {
                                submitted = true
                                viewModel.recordQuizAttempt(
                                    testTitle = "Prelims Drill ($activeQuizMode)",
                                    testType = "PRELIMS_MOCK",
                                    chapterId = "ncert_hist_6_ch1",
                                    questions = quizQuestions,
                                    selectedAnswers = selectedAnswers.toMap(),
                                    elapsedSeconds = timerSeconds
                                )
                            },
                            enabled = selectedAnswers.isNotEmpty() && !submitted,
                            modifier = Modifier.testTag("submit_prelims_mock_button")
                        ) {
                            Text(if (submitted) "Saved ✔" else "Submit Mock")
                        }
                    }
                }
            }

            items(quizQuestions, key = { it.questionId }) { q ->
                val chosen = selectedAnswers[q.questionId]
                val reveal = submitted || chosen != null
                PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (q.pyqYear != null) "${q.sourceRef} (${q.pyqYear})" else q.category.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(q.questionText, style = MaterialTheme.typography.titleSmall)
                        Text(q.questionTextHi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        q.options.forEachIndexed { idx, opt ->
                            val isSel = chosen == idx
                            val isAns = idx == q.correctIndex
                            val bg = when {
                                reveal && isAns -> SuccessMintContainer
                                reveal && isSel && !isAns -> ErrorRoseContainer
                                isSel -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            }
                            Surface(
                                color = bg,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().clickable(enabled = !submitted) {
                                    selectedAnswers[q.questionId] = idx
                                }
                            ) {
                                Text(
                                    text = "${('A' + idx)}. $opt",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = when {
                                        reveal && isAns -> SuccessMint
                                        reveal && isSel && !isAns -> ErrorRose
                                        else -> MaterialTheme.colorScheme.onSurface
                                    },
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                        if (reveal) {
                            Text("व्याख्या: ${q.explanationHi}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        // Past Test History & Weak Topics
        item {
            Text("Recent Test History (${pastAttempts.size})", style = MaterialTheme.typography.titleMedium)
        }

        items(pastAttempts.take(10), key = { it.id }) { att ->
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(att.testTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("${att.netScore}/${att.maxScore}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(
                        text = "Right: ${att.correctCount} • Wrong: ${att.wrongCount} • Skipped: ${att.skippedCount} • Accuracy: ${att.accuracyPercent}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (att.weakTopics.isNotEmpty()) {
                        Text(
                            text = "Revise Weak Concepts: ${att.weakTopics.joinToString(", ")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = ErrorRose
                        )
                    }
                }
            }
        }
    }
}
