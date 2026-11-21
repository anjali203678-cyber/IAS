package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.CivilsDatabase
import com.example.data.model.ReminderConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        if (!StudyReminderScheduler.hasNotificationPermission(context)) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = CivilsDatabase.getInstance(context).civilsDao()
                val enabledReminders = dao.getEnabledRemindersOnce()
                enabledReminders.forEach { entity ->
                    StudyReminderScheduler.scheduleReminder(
                        context,
                        ReminderConfig(
                            category = entity.category,
                            title = entity.title,
                            hour = entity.hour,
                            minute = entity.minute,
                            enabled = entity.enabled
                        )
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
