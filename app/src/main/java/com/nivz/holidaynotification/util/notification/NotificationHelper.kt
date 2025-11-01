package com.nivz.holidaynotification.util.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nivz.holidaynotification.R
import com.nivz.holidaynotification.data.model.Holiday
import com.nivz.holidaynotification.service.HolidayNotificationReceiver
import java.util.Calendar

/**
 * Helper class for managing notifications
 * Handles creation, scheduling, and display of holiday notifications
 */
object NotificationHelper {

    private const val CHANNEL_ID = "holiday_notifications"
    private const val CHANNEL_NAME = "Holiday Notifications"
    private const val NOTIFICATION_ID_BASE = 1000

    /**
     * Create notification channel (required for Android 8+)
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for holidays and special dates"
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Schedule a notification for a holiday
     */
    fun scheduleNotification(context: Context, holiday: Holiday) {
        if (!holiday.notificationEnabled) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, holiday.month - 1) // Calendar.MONTH is 0-based
            set(Calendar.DAY_OF_MONTH, holiday.day)
            set(Calendar.HOUR_OF_DAY, holiday.notificationHour)
            set(Calendar.MINUTE, holiday.notificationMinute)
            set(Calendar.SECOND, 0)

            // If the time has already passed today, schedule for next year
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.YEAR, 1)
            }

            // Subtract notification days
            add(Calendar.DAY_OF_MONTH, -holiday.notificationDays)
        }

        val intent = Intent(context, HolidayNotificationReceiver::class.java).apply {
            action = "com.nivz.holidaynotification.SHOW_NOTIFICATION"
            putExtra("holiday_id", holiday.id)
            putExtra("holiday_name", holiday.name)
            putExtra("holiday_description", holiday.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            holiday.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // For Android 12+
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Fallback if exact alarm permission is not available
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    /**
     * Cancel a scheduled notification
     */
    fun cancelNotification(context: Context, holiday: Holiday) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, HolidayNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            holiday.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    /**
     * Show an immediate notification
     */
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = NOTIFICATION_ID_BASE
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .setLights(0xFF5722.toInt(), 3000, 3000)
            .build()

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Reschedule all notifications
     * Call this when app starts or after updating notification settings
     */
    suspend fun rescheduleAllNotifications(context: Context, holidays: List<Holiday>) {
        holidays.forEach { holiday ->
            cancelNotification(context, holiday)
            scheduleNotification(context, holiday)
        }
    }
}
