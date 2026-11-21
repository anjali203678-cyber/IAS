package com.example

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.notifications.StudyReminderScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app name matches CIVILS IAS`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CIVILS IAS", appName)
    }

    @Test
    fun `notification channel is created and next trigger is in the future`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        StudyReminderScheduler.ensureChannel(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = manager.getNotificationChannel(StudyReminderScheduler.CHANNEL_ID)
        assertNotNull(channel)
        assertEquals("CIVILS IAS Study Reminders", channel.name.toString())

        val now = 1_700_000_000_000L
        val next = StudyReminderScheduler.computeNextTriggerMillis(6, 0, now)
        assertTrue("Trigger millis must be strictly after now", next > now)
    }
}
