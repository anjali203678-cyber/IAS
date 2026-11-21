package com.example.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.ReminderCategory
import com.example.data.model.ReminderConfig
import java.util.Calendar

object StudyReminderScheduler {

    const val CHANNEL_ID = "civils_ias_study_reminders"
    const val EXTRA_CATEGORY = "extra_reminder_category"
    const val EXTRA_TITLE = "extra_reminder_title"
    const val EXTRA_MESSAGE = "extra_reminder_message"
    const val EXTRA_HOUR = "extra_reminder_hour"
    const val EXTRA_MINUTE = "extra_reminder_minute"

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "CIVILS IAS Study Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Daily 9-hour UPSC study schedule, English practice, MCQ & spaced revision reminders"
                    enableVibration(true)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    fun scheduleReminder(context: Context, config: ReminderConfig) {
        if (!hasNotificationPermission(context)) return
        ensureChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(context, config)

        if (!config.enabled) {
            alarmManager.cancel(pendingIntent)
            return
        }

        val triggerAtMillis = computeNextTriggerMillis(config.hour, config.minute)
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    fun cancelReminder(context: Context, category: String, title: String = "Study Reminder") {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dummy = ReminderConfig(category = category, title = title, hour = 6, minute = 0, enabled = false)
        alarmManager.cancel(buildPendingIntent(context, dummy))
    }

    fun computeNextTriggerMillis(hour: Int, minute: Int, nowMillis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, hour.coerceIn(0, 23))
            set(Calendar.MINUTE, minute.coerceIn(0, 59))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (cal.timeInMillis <= nowMillis) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return cal.timeInMillis
    }

    fun showNotification(context: Context, category: String, title: String, message: String) {
        if (!hasNotificationPermission(context)) return
        ensureChannel(context)

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentIntent = PendingIntent.getActivity(
            context,
            category.hashCode(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("CIVILS IAS • $title")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(0xFF30204F.toInt())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(category.hashCode(), notification)
        } catch (_: SecurityException) {
            // Permission revoked before firing
        }
    }

    private fun buildPendingIntent(context: Context, config: ReminderConfig): PendingIntent {
        val catEnum = ReminderCategory.entries.firstOrNull { it.id == config.category }
        val message = catEnum?.messageBody ?: "Stay consistent with your UPSC CSE 2029 Purple Dream plan!"
        val intent = Intent(context, StudyReminderReceiver::class.java).apply {
            putExtra(EXTRA_CATEGORY, config.category)
            putExtra(EXTRA_TITLE, config.title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_HOUR, config.hour)
            putExtra(EXTRA_MINUTE, config.minute)
        }
        return PendingIntent.getBroadcast(
            context,
            config.category.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
