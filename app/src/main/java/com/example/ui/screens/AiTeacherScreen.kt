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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.viewmodel.AiTeacherViewModel

@Composable
fun AiTeacherScreen(
    aiViewModel: AiTeacherViewModel,
    initialPrompt: String? = null
) {
    val messages by aiViewModel.messages.collectAsStateWithLifecycle()
    val streamingReply by aiViewModel.streamingReply.collectAsStateWithLifecycle()
    val isThinking by aiViewModel.isThinking.collectAsStateWithLifecycle()
    val chatError by aiViewModel.chatError.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf("TEACH") }

    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            inputText = initialPrompt
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header + Mode Selector
        Surface(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f), tonalElevation = 2.dp) {
            Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Psychology, contentDescription = "AI Teacher", tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("CIVILS IAS Personal AI Teacher", style = MaterialTheme.typography.titleMedium)
                        Text("Simple Hindi + Gradual English • Powered by Gemini", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "TEACH" to "Teach Basics",
                        "MEMORY" to "Memory Trick",
                        "ORAL_REVISION" to "Oral Revision",
                        "ENGLISH_PRACTICE" to "English Practice"
                    ).forEach { (mode, label) ->
                        FilterChip(
                            selected = selectedMode == mode,
                            onClick = { selectedMode = mode },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }

        // Conversation Stream
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { turn ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (turn.isUser) Arrangement.End else Arrangement.Start
                ) {
                    PurpleDreamCard(
                        modifier = Modifier.fillMaxWidth(0.88f),
                        containerColor = if (turn.isUser) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (turn.isUser) "You" else "AI Teacher",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(turn.text, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth(0.88f)) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(Modifier.width(8.dp))
                                Text("AI Teacher is writing...", style = MaterialTheme.typography.labelMedium)
                            }
                            if (streamingReply.isNotBlank()) {
                                Text(streamingReply, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            if (chatError != null) {
                item {
                    PurpleDreamCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = chatError!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Input Composer
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask any UPSC doubt in Hindi or English...") },
                    modifier = Modifier.weight(1f).testTag("ai_chat_input"),
                    maxLines = 3,
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        val msg = inputText
                        inputText = ""
                        aiViewModel.sendMessage(msg, selectedMode)
                    },
                    enabled = inputText.isNotBlank() && !isThinking,
                    modifier = Modifier.size(48.dp).testTag("ai_chat_send_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send question to AI Teacher",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
