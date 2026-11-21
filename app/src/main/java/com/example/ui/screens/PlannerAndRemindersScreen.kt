package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ReminderConfig
import com.example.data.model.StudyTask
import com.example.data.model.TaskStatus
import com.example.data.model.UiState
import com.example.notifications.StudyReminderScheduler
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.theme.SuccessMint
import com.example.ui.viewmodel.CivilsViewModel

@Composable
fun PlannerAndRemindersScreen(
    viewModel: CivilsViewModel,
    initialTab: Int = 0
) {
    val context = LocalContext.current
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }

    val tasksState by viewModel.studyTasksState.collectAsStateWithLifecycle()
    val remindersState by viewModel.remindersState.collectAsStateWithLifecycle()
    val activeTimerId by viewModel.activeTimerTaskId.collectAsStateWithLifecycle()
    val activeSeconds by viewModel.activeTimerElapsedSeconds.collectAsStateWithLifecycle()

    var hasNotifPerm by remember { mutableStateOf(StudyReminderScheduler.hasNotificationPermission(context)) }
    val permLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasNotifPerm = granted || StudyReminderScheduler.hasNotificationPermission(context)
    }

    var editingTask by remember { mutableStateOf<StudyTask?>(null) }
    var showTaskDialog by remember { mutableStateOf(false) }
    var editingReminder by remember { mutableStateOf<ReminderConfig?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("9-Hour Study Plan") },
                modifier = Modifier.testTag("planner_tab")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("11 Study Reminders") },
                modifier = Modifier.testTag("reminders_tab")
            )
        }

        if (selectedTab == 0) {
            val todayTasks = remember(tasksState, viewModel.todayDate) {
                ((tasksState as? UiState.Success)?.data ?: emptyList())
                    .filter { it.taskDate == viewModel.todayDate }
                    .sortedBy { it.orderIndex }
            }
            val plannedMinutes = todayTasks.sumOf { it.durationMinutes }
            val completedMinutes = todayTasks.sumOf {
                if (it.status == TaskStatus.COMPLETED.name) it.actualMinutes else 0
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Daily 9-Hour UPSC Target", style = MaterialTheme.typography.titleLarge)
                                Button(
                                    onClick = {
                                        editingTask = null
                                        showTaskDialog = true
                                    },
                                    modifier = Modifier.testTag("add_study_task_button")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add block")
                                    Spacer(Modifier.width(4.dp))
                                    Text("Add Block")
                                }
                            }
                            Text(
                                text = "Planned: ${plannedMinutes / 60}h ${plannedMinutes % 60}m ($plannedMinutes min) • Completed: ${completedMinutes / 60}h ${completedMinutes % 60}m • Breaks scheduled separately",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            LinearProgressIndicator(
                                progress = { (completedMinutes / 540f).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50))
                            )
                            if (activeTimerId != null) {
                                Text(
                                    text = "⏱️ Live Study Timer: %02d:%02d".format(activeSeconds / 60, activeSeconds % 60),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                items(todayTasks, key = { it.id }) { task ->
                    val isRunning = activeTimerId == task.id
                    val isDone = task.status == TaskStatus.COMPLETED.name
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${task.scheduledTime} • ${task.durationMinutes} min • ${task.category}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(task.title, style = MaterialTheme.typography.titleSmall)
                                    Text(task.titleHi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Row {
                                    IconButton(onClick = {
                                        editingTask = task
                                        showTaskDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit task")
                                    }
                                    if (task.custom) {
                                        IconButton(onClick = { viewModel.deleteTask(task.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete custom task")
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!isDone) {
                                    if (isRunning) {
                                        OutlinedButton(
                                            onClick = { viewModel.pauseActiveTask(task) },
                                            modifier = Modifier.weight(1f).testTag("pause_task_${task.id}")
                                        ) {
                                            Icon(Icons.Default.Pause, contentDescription = "Pause")
                                            Spacer(Modifier.width(4.dp))
                                            Text("Pause (${activeSeconds / 60}m)")
                                        }
                                    } else {
                                        OutlinedButton(
                                            onClick = { viewModel.startOrResumeTaskTimer(task) },
                                            modifier = Modifier.weight(1f).testTag("start_task_${task.id}")
                                        ) {
                                            Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                                            Spacer(Modifier.width(4.dp))
                                            Text(if (task.actualMinutes > 0) "Resume (${task.actualMinutes}m)" else "Start Timer")
                                        }
                                    }
                                    Button(
                                        onClick = { viewModel.completeStudyTask(task) },
                                        modifier = Modifier.weight(1f).testTag("complete_task_${task.id}")
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Complete")
                                        Spacer(Modifier.width(4.dp))
                                        Text("Done")
                                    }
                                } else {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "Completed ✔ (${task.actualMinutes} min logged)",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = SuccessMint,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val reminders = ((remindersState as? UiState.Success)?.data ?: emptyList())
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = "Reminders", tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text("Smart Study Notification Bells", style = MaterialTheme.typography.titleLarge)
                            }
                            Text(
                                text = if (hasNotifPerm) {
                                    "Notification permission granted ✔. Toggle any of the 11 UPSC study reminders below."
                                } else {
                                    "Notification permission is required on Android 13+ before study alarms can fire."
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                if (!hasNotifPerm && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    Button(
                                        onClick = { permLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                                        modifier = Modifier.weight(1f).testTag("grant_notification_permission_button")
                                    ) {
                                        Text("Allow Notifications")
                                    }
                                }
                                OutlinedButton(
                                    onClick = { viewModel.sendTestNotification(context) },
                                    modifier = Modifier.weight(1f).testTag("send_test_notification_button")
                                ) {
                                    Text("Send Test Bell")
                                }
                            }
                        }
                    }
                }

                items(reminders, key = { it.category }) { config ->
                    PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(config.title, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    text = "Scheduled at %02d:%02d daily".format(config.hour, config.minute),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { editingReminder = config }) {
                                Icon(Icons.Default.Schedule, contentDescription = "Change reminder time")
                            }
                            Switch(
                                checked = config.enabled && hasNotifPerm,
                                onCheckedChange = { checked ->
                                    if (checked && !hasNotifPerm && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.updateReminder(context, config, checked, config.hour, config.minute)
                                    }
                                },
                                modifier = Modifier.testTag("reminder_switch_${config.category}")
                            )
                        }
                    }
                }
            }
        }
    }

    if (showTaskDialog) {
        StudyTaskDialog(
            existing = editingTask,
            onDismiss = { showTaskDialog = false },
            onSave = { title, cat, mins, time ->
                viewModel.saveCustomOrEditedTask(editingTask, title, cat, mins, time)
                showTaskDialog = false
            }
        )
    }

    editingReminder?.let { rem ->
        ReminderTimeDialog(
            config = rem,
            onDismiss = { editingReminder = null },
            onSave = { h, m ->
                viewModel.updateReminder(context, rem, rem.enabled, h, m)
                editingReminder = null
            }
        )
    }
}

@Composable
private fun StudyTaskDialog(
    existing: StudyTask?,
    onDismiss: () -> Unit,
    onSave: (String, String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf(existing?.title ?: "") }
    var category by remember { mutableStateOf(existing?.category ?: "CUSTOM") }
    var minutes by remember { mutableStateOf((existing?.durationMinutes ?: 60).toString()) }
    var time by remember { mutableStateOf(existing?.scheduledTime ?: "18:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "Add Study Block" else "Edit / Reschedule Block") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Topic / Subject Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g. FOUNDATION, REVISION)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter(Char::isDigit) },
                    label = { Text("Duration (minutes)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Scheduled Time (HH:mm)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title.ifBlank { "Custom Study Block" }, category, minutes.toIntOrNull() ?: 60, time) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ReminderTimeDialog(
    config: ReminderConfig,
    onDismiss: () -> Unit,
    onSave: (Int, Int) -> Unit
) {
    var hourText by remember { mutableStateOf(config.hour.toString()) }
    var minText by remember { mutableStateOf(config.minute.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Time: ${config.title}") },
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = hourText,
                    onValueChange = { hourText = it.filter(Char::isDigit).take(2) },
                    label = { Text("Hour (0–23)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = minText,
                    onValueChange = { minText = it.filter(Char::isDigit).take(2) },
                    label = { Text("Min (0–59)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    (hourText.toIntOrNull() ?: config.hour).coerceIn(0, 23),
                    (minText.toIntOrNull() ?: config.minute).coerceIn(0, 59)
                )
            }) {
                Text("Save Time")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
