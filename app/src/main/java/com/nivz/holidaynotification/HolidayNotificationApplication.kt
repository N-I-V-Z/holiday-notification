package com.nivz.holidaynotification

import android.app.Application
import com.nivz.holidaynotification.util.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for HolidayNotification app
 * Initializes Hilt dependency injection and other global setup
 */
@HiltAndroidApp
class HolidayNotificationApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Create notification channel on app startup
        NotificationHelper.createNotificationChannel(this)
    }
}
