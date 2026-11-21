package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UiState
import com.example.data.seed.AcademiesSeed
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.viewmodel.AiEvalState
import com.example.ui.viewmodel.AiTeacherViewModel
import com.example.ui.viewmodel.CivilsViewModel

@Composable
fun SpecializedAcademiesScreen(
    viewModel: CivilsViewModel,
    aiViewModel: AiTeacherViewModel
) {
    var selectedAcademy by remember { mutableStateOf("MAINS") } // MAINS, ESSAY, CA, ENGLISH, INTERVIEW
    val evalState by aiViewModel.evalState.collectAsStateWithLifecycle()
    val englishLogsState by viewModel.englishLogsState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "MAINS" to "Mains Zone",
                    "ESSAY" to "Essay Academy",
                    "CA" to "Current Affairs",
                    "ENGLISH" to "English (45m)",
                    "INTERVIEW" to "Interview"
                ).forEach { (key, label) ->
                    FilterChip(
                        selected = selectedAcademy == key,
                        onClick = {
                            selectedAcademy = key
                            aiViewModel.clearEval()
                        },
                        label = { Text(label) },
                        modifier = Modifier.testTag("academy_chip_$key")
                    )
                }
            }
        }

        when (selectedAcademy) {
            "MAINS" -> {
                items(AcademiesSeed.mainsPrompts, key = { it.id }) { prompt ->
                    var answerDraft by remember(prompt.id) { mutableStateOf("") }
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("${prompt.paper} • ${prompt.marks} Marks • ${prompt.wordLimit} Words", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Text(prompt.question, style = MaterialTheme.typography.titleSmall)
                            Text(prompt.questionHi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Surface(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), shape = RoundedCornerShape(10.dp)) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Structure Guide:", style = MaterialTheme.typography.labelLarge)
                                    Text("Intro: ${prompt.introTip}", style = MaterialTheme.typography.bodySmall)
                                    prompt.bodyStructure.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
                                    Text("Conclusion: ${prompt.conclusionTip}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            OutlinedTextField(
                                value = answerDraft,
                                onValueChange = { answerDraft = it },
                                label = { Text("Write your practice answer (Hindi or English)") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                            Button(
                                onClick = { aiViewModel.evaluateWrittenResponse(prompt.question, answerDraft, "Mains GS/Optional") },
                                modifier = Modifier.fillMaxWidth().testTag("eval_mains_${prompt.id}")
                            ) {
                                Icon(Icons.Default.EditNote, contentDescription = "Evaluate Mains answer")
                                Spacer(Modifier.width(6.dp))
                                Text("Get AI Examiner Feedback")
                            }
                        }
                    }
                }
            }

            "ESSAY" -> {
                items(AcademiesSeed.essayPrompts, key = { it.id }) { essay ->
                    var essayDraft by remember(essay.id) { mutableStateOf("") }
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(essay.sourceLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Text("\"${essay.topic}\"", style = MaterialTheme.typography.titleMedium)
                            Text(essay.topicHi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Guide: ${essay.interpretationGuide}", style = MaterialTheme.typography.bodySmall)
                            Text("Dimensions: ${essay.dimensions.joinToString(" • ")}", style = MaterialTheme.typography.labelSmall)
                            Text("Verified Quote: ${essay.verifiedQuoteSuggestion}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            OutlinedTextField(
                                value = essayDraft,
                                onValueChange = { essayDraft = it },
                                label = { Text("Draft essay outline or intro paragraph") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                            Button(
                                onClick = { aiViewModel.evaluateWrittenResponse(essay.topic, essayDraft, "UPSC Essay") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = "Evaluate Essay")
                                Spacer(Modifier.width(6.dp))
                                Text("Evaluate Essay Outline with AI")
                            }
                        }
                    }
                }
            }

            "CA" -> {
                item {
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Newspaper, contentDescription = "Current Affairs", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Verifiable Curated UPSC Briefs (Dated & Sourced — no fabricated live headlines)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                items(AcademiesSeed.currentAffairsArchive, key = { it.id }) { ca ->
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("${ca.category} • ${ca.publishedDate}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Text(ca.title, style = MaterialTheme.typography.titleMedium)
                            Text("Source: ${ca.source} • Syllabus: ${ca.syllabusTopic}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("What happened: ${ca.whatHappened}", style = MaterialTheme.typography.bodySmall)
                            Text("Why important: ${ca.whyImportant}", style = MaterialTheme.typography.bodySmall)
                            Text("Prelims link: ${ca.prelimsRelevance}", style = MaterialTheme.typography.bodySmall)
                            Text("Mains GS link: ${ca.mainsRelevance}", style = MaterialTheme.typography.bodySmall)
                            ca.importantFacts.forEach { fact ->
                                Text("★ $fact", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }

            "ENGLISH" -> {
                item {
                    var mins by remember { mutableStateOf("45") }
                    var vocab by remember { mutableStateOf("10") }
                    var mistakes by remember { mutableStateOf("3") }
                    var confidence by remember { mutableStateOf("7") }
                    var speechDraft by remember { mutableStateOf("") }
                    val logs = (englishLogsState as? UiState.Success)?.data ?: emptyList()

                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Translate, contentDescription = "English Academy", tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text("Daily 45-Min English Speaking Academy", style = MaterialTheme.typography.titleMedium)
                            }
                            Text("Total sessions logged: ${logs.size}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(value = mins, onValueChange = { mins = it.filter(Char::isDigit) }, label = { Text("Min") }, modifier = Modifier.weight(1f), singleLine = true)
                                OutlinedTextField(value = vocab, onValueChange = { vocab = it.filter(Char::isDigit) }, label = { Text("Words") }, modifier = Modifier.weight(1f), singleLine = true)
                                OutlinedTextField(value = mistakes, onValueChange = { mistakes = it.filter(Char::isDigit) }, label = { Text("Fixed") }, modifier = Modifier.weight(1f), singleLine = true)
                                OutlinedTextField(value = confidence, onValueChange = { confidence = it.filter(Char::isDigit) }, label = { Text("/10") }, modifier = Modifier.weight(1f), singleLine = true)
                            }
                            OutlinedTextField(
                                value = speechDraft,
                                onValueChange = { speechDraft = it },
                                label = { Text("Practice speaking/writing sentences in English") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = {
                                        viewModel.logEnglishPractice(
                                            mins.toIntOrNull() ?: 45,
                                            vocab.toIntOrNull() ?: 10,
                                            mistakes.toIntOrNull() ?: 2,
                                            confidence.toIntOrNull() ?: 7,
                                            speechDraft
                                        )
                                    },
                                    modifier = Modifier.weight(1f).testTag("log_english_practice_button")
                                ) {
                                    Text("Log 45m (+2★)")
                                }
                                OutlinedButton(
                                    onClick = { aiViewModel.evaluateWrittenResponse("Daily English Speaking Practice", speechDraft, "English Grammar & Fluency") },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Check Grammar")
                                }
                            }
                        }
                    }
                }

                items(AcademiesSeed.englishLessons, key = { it.id }) { lesson ->
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(lesson.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Focus: ${lesson.focusArea}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Text("Grammar Rule: ${lesson.grammarRule}", style = MaterialTheme.typography.bodySmall)
                            Text("Hindi → English Drills:", style = MaterialTheme.typography.labelLarge)
                            lesson.hindiToEnglishDrills.forEach { (hi, en) ->
                                Text("• $hi\n  → $en", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("Key Vocabulary:", style = MaterialTheme.typography.labelLarge)
                            lesson.keyVocabulary.forEach { (w, m, ex) ->
                                Text("• $w ($m): $ex", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("Speaking Prompt: ${lesson.speakingPrompt}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            else -> {
                items(AcademiesSeed.interviewQuestions, key = { it.id }) { iq ->
                    var respDraft by remember(iq.id) { mutableStateOf("") }
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = "Interview", tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text(iq.category, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(iq.question, style = MaterialTheme.typography.titleSmall)
                            Text("Balanced Approach: ${iq.idealApproach}", style = MaterialTheme.typography.bodySmall)
                            Text("Avoid: ${iq.pitfallsToAvoid}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedTextField(
                                value = respDraft,
                                onValueChange = { respDraft = it },
                                label = { Text("Draft your interview response") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2
                            )
                            Button(
                                onClick = { aiViewModel.evaluateWrittenResponse(iq.question, respDraft, "UPSC Personality Test / Interview") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Get Mock Board AI Feedback")
                            }
                        }
                    }
                }
            }
        }

        // Shared AI Evaluation Result Box
        if (evalState !is AiEvalState.Idle) {
            item {
                PurpleDreamCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("AI Examiner Feedback", style = MaterialTheme.typography.titleMedium)
                        when (val s = evalState) {
                            is AiEvalState.Loading -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                                    Spacer(Modifier.width(10.dp))
                                    Text("Evaluating your response carefully...")
                                }
                            }
                            is AiEvalState.Success -> Text(s.feedback, style = MaterialTheme.typography.bodyMedium)
                            is AiEvalState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                            AiEvalState.Idle -> {}
                        }
                    }
                }
            }
        }
    }
}
