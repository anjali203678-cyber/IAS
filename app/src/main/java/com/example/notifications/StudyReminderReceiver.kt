package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.model.ReminderConfig

class StudyReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val category = intent.getStringExtra(StudyReminderScheduler.EXTRA_CATEGORY) ?: "MORNING_START"
        val title = intent.getStringExtra(StudyReminderScheduler.EXTRA_TITLE) ?: "Study Reminder"
        val message = intent.getStringExtra(StudyReminderScheduler.EXTRA_MESSAGE)
            ?: "Time for your next UPSC 2029 study milestone!"
        val hour = intent.getIntExtra(StudyReminderScheduler.EXTRA_HOUR, 6)
        val minute = intent.getIntExtra(StudyReminderScheduler.EXTRA_MINUTE, 0)

        StudyReminderScheduler.showNotification(context, category, title, message)

        // Reschedule for next day
        StudyReminderScheduler.scheduleReminder(
            context,
            ReminderConfig(
                category = category,
                title = title,
                hour = hour,
                minute = minute,
                enabled = true
            )
        )
    }
}
