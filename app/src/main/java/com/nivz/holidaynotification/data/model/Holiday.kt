package com.nivz.holidaynotification.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Holiday entity representing a holiday in the calendar
 * Can be either a built-in Vietnamese holiday or a custom user holiday
 */
@Entity(tableName = "holidays")
data class Holiday(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",

    // Date information (lunar or solar)
    val day: Int,          // 1-31
    val month: Int,        // 1-12
    val year: Int? = null, // null means recurring every year

    // Holiday type
    val isLunar: Boolean = false, // false = solar, true = lunar
    val isCustom: Boolean = false, // false = built-in, true = custom

    // Notification settings
    val notificationEnabled: Boolean = true,
    val notificationDays: Int = 0, // Days before holiday to notify
    val notificationHour: Int = 0, // Hour of day (0-23)
    val notificationMinute: Int = 0, // Minute (0-59)

    // Color for UI
    val colorCode: String = "#FF5722", // Default color

    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
