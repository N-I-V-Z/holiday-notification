package com.nivz.holidaynotification.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nivz.holidaynotification.util.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * BroadcastReceiver for receiving notification intents from AlarmManager
 * Displays notifications when alarm triggers
 * Works even when app is not running
 */
@AndroidEntryPoint
class HolidayNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // If alarm was set for boot completion, reschedule notifications
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // TODO: Reschedule all notifications when device boots
            return
        }

        // Handle notification display
        if (intent.action == "com.nivz.holidaynotification.SHOW_NOTIFICATION") {
            val holidayName = intent.getStringExtra("holiday_name") ?: "Holiday"
            val holidayDescription = intent.getStringExtra("holiday_description") ?: ""

            val title = holidayName
            val message = if (holidayDescription.isNotEmpty()) {
                "$holidayDescription - Today!"
            } else {
                "Today is $holidayName!"
            }

            // Show notification
            NotificationHelper.createNotificationChannel(context)
            NotificationHelper.showNotification(context, title, message)

            // Optionally log or send analytics
            logNotificationEvent(holidayName)
        }
    }

    /**
     * Log notification event (could be sent to analytics service)
     */
    private fun logNotificationEvent(holidayName: String) {
        // TODO: Implement analytics or logging
        // For now, just print to logcat in debug builds
    }
}
