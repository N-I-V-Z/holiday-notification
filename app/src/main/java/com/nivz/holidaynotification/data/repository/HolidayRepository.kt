package com.nivz.holidaynotification.data.repository

import androidx.lifecycle.LiveData
import com.nivz.holidaynotification.data.database.HolidayDao
import com.nivz.holidaynotification.data.model.Holiday
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing holiday data
 * Acts as a single source of truth for holiday information
 */
@Singleton
class HolidayRepository @Inject constructor(
    private val holidayDao: HolidayDao
) {

    // LiveData for UI observations
    fun getAllHolidaysLiveData(): LiveData<List<Holiday>> =
        holidayDao.getAllHolidaysLiveData()

    fun getCustomHolidaysLiveData(): LiveData<List<Holiday>> =
        holidayDao.getCustomHolidaysLiveData()

    // Add a new holiday
    suspend fun addHoliday(holiday: Holiday): Long =
        holidayDao.insertHoliday(holiday)

    // Add multiple holidays (used for initial data population)
    suspend fun addHolidays(holidays: List<Holiday>) =
        holidayDao.insertHolidays(holidays)

    // Update existing holiday
    suspend fun updateHoliday(holiday: Holiday) =
        holidayDao.updateHoliday(holiday)

    // Delete a holiday
    suspend fun deleteHoliday(holiday: Holiday) =
        holidayDao.deleteHoliday(holiday)

    // Delete by ID
    suspend fun deleteHolidayById(id: Int) =
        holidayDao.deleteHolidayById(id)

    // Delete all custom holidays
    suspend fun deleteAllCustomHolidays() =
        holidayDao.deleteAllCustomHolidays()

    // Get holiday by ID
    suspend fun getHolidayById(id: Int): Holiday? =
        holidayDao.getHolidayById(id)

    // Get holidays for a specific date
    suspend fun getHolidaysByDate(month: Int, day: Int): List<Holiday> =
        holidayDao.getHolidaysByDate(month, day)

    // Get solar calendar holidays for a date
    suspend fun getSolarHolidaysByDate(month: Int, day: Int): List<Holiday> =
        holidayDao.getSolarHolidaysByDate(month, day)

    // Get lunar calendar holidays for a date
    suspend fun getLunarHolidaysByDate(month: Int, day: Int): List<Holiday> =
        holidayDao.getLunarHolidaysByDate(month, day)

    // Get all holidays
    suspend fun getAllHolidays(): List<Holiday> =
        holidayDao.getAllHolidays()

    // Get holidays with enabled notifications
    suspend fun getEnabledNotifications(): List<Holiday> =
        holidayDao.getEnabledNotifications()

    // Get total count of holidays
    suspend fun getHolidayCount(): Int =
        holidayDao.getHolidayCount()

    // Get recurring holidays (year = null)
    suspend fun getRecurringHolidays(): List<Holiday> =
        holidayDao.getRecurringHolidays()

    // Check if holiday exists on a date
    suspend fun hasHolidayOnDate(month: Int, day: Int): Boolean =
        holidayDao.getHolidaysByDate(month, day).isNotEmpty()

    // Delete all built-in holidays (preserves custom holidays)
    suspend fun deleteAllBuiltInHolidays() =
        holidayDao.deleteAllBuiltInHolidays()
}
