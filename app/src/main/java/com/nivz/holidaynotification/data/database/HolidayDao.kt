package com.nivz.holidaynotification.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nivz.holidaynotification.data.model.Holiday

/**
 * Data Access Object for Holiday entity
 * Handles all database operations for holidays
 */
@Dao
interface HolidayDao {

    // Insert operations
    @Insert
    suspend fun insertHoliday(holiday: Holiday): Long

    @Insert
    suspend fun insertHolidays(holidays: List<Holiday>)

    // Update operations
    @Update
    suspend fun updateHoliday(holiday: Holiday)

    // Delete operations
    @Delete
    suspend fun deleteHoliday(holiday: Holiday)

    @Query("DELETE FROM holidays WHERE id = :id")
    suspend fun deleteHolidayById(id: Int)

    @Query("DELETE FROM holidays WHERE isCustom = 1")
    suspend fun deleteAllCustomHolidays()

    // Query operations
    @Query("SELECT * FROM holidays WHERE id = :id")
    suspend fun getHolidayById(id: Int): Holiday?

    @Query("SELECT * FROM holidays ORDER BY month, day")
    fun getAllHolidaysLiveData(): LiveData<List<Holiday>>

    @Query("SELECT * FROM holidays ORDER BY month, day")
    suspend fun getAllHolidays(): List<Holiday>

    @Query("SELECT * FROM holidays WHERE month = :month AND day = :day")
    suspend fun getHolidaysByDate(month: Int, day: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE isCustom = 1 ORDER BY createdAt DESC")
    fun getCustomHolidaysLiveData(): LiveData<List<Holiday>>

    @Query("SELECT * FROM holidays WHERE isLunar = 0 AND month = :month AND day = :day")
    suspend fun getSolarHolidaysByDate(month: Int, day: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE isLunar = 1 AND month = :month AND day = :day")
    suspend fun getLunarHolidaysByDate(month: Int, day: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE notificationEnabled = 1")
    suspend fun getEnabledNotifications(): List<Holiday>

    @Query("SELECT COUNT(*) FROM holidays")
    suspend fun getHolidayCount(): Int

    @Query("SELECT * FROM holidays WHERE year IS NULL ORDER BY month, day")
    suspend fun getRecurringHolidays(): List<Holiday>

    @Query("DELETE FROM holidays WHERE isCustom = 0")
    suspend fun deleteAllBuiltInHolidays()
}
