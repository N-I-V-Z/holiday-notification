package com.nivz.holidaynotification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nivz.holidaynotification.data.model.Holiday
import com.nivz.holidaynotification.data.repository.HolidayRepository
import com.nivz.holidaynotification.util.calendar.LunarCalendarConverter
import com.nivz.holidaynotification.util.calendar.VietnameseHolidaysProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing holiday data and operations
 * Handles all business logic related to holidays
 */
@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val repository: HolidayRepository
) : ViewModel() {

    // Live data for all holidays
    val allHolidays: LiveData<List<Holiday>> = repository.getAllHolidaysLiveData()
    val customHolidays: LiveData<List<Holiday>> = repository.getCustomHolidaysLiveData()

    // State management
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _holidaysForSelectedDate = MutableLiveData<List<Holiday>>(emptyList())
    val holidaysForSelectedDate: LiveData<List<Holiday>> = _holidaysForSelectedDate

    // Track current selected date
    private val _selectedDay = MutableLiveData(1)
    val selectedDay: LiveData<Int> = _selectedDay

    private val _selectedMonth = MutableLiveData(1)
    val selectedMonth: LiveData<Int> = _selectedMonth

    private val _selectedYear = MutableLiveData(2024)
    val selectedYear: LiveData<Int> = _selectedYear

    init {
        initializeDatabase()
    }

    /**
     * Initialize database with built-in Vietnamese holidays
     * Clears old built-in holidays and reloads from VietnameseHolidaysProvider
     * Preserves user's custom holidays (isCustom = true)
     */
    private fun initializeDatabase() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val recurringHolidayCount = repository.getRecurringHolidays().size

                // Reload built-in holidays if count doesn't match (handles data updates and app rebuilds)
                // We expect 33 built-in holidays from VietnameseHolidaysProvider
                if (recurringHolidayCount != 33) {
                    // Delete all built-in holidays at once (preserves user's custom holidays)
                    repository.deleteAllBuiltInHolidays()

                    // Reload fresh built-in holidays
                    val vietnameseHolidays = VietnameseHolidaysProvider.getVietnameseHolidays()
                    repository.addHolidays(vietnameseHolidays)
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to initialize holidays: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    /**
     * Add a new custom holiday
     */
    fun addHoliday(holiday: Holiday) {
        viewModelScope.launch {
            try {
                repository.addHoliday(holiday)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add holiday: ${e.message}"
            }
        }
    }

    /**
     * Update an existing holiday
     */
    fun updateHoliday(holiday: Holiday) {
        viewModelScope.launch {
            try {
                repository.updateHoliday(holiday)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update holiday: ${e.message}"
            }
        }
    }

    /**
     * Delete a holiday
     */
    fun deleteHoliday(holiday: Holiday) {
        viewModelScope.launch {
            try {
                repository.deleteHoliday(holiday)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete holiday: ${e.message}"
            }
        }
    }

    /**
     * Select a date and fetch holidays for that date
     */
    fun selectDate(day: Int, month: Int, year: Int) {
        _selectedDay.value = day
        _selectedMonth.value = month
        _selectedYear.value = year

        viewModelScope.launch {
            try {
                val holidays = repository.getHolidaysByDate(month, day)
                _holidaysForSelectedDate.value = holidays
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch holidays: ${e.message}"
            }
        }
    }

    /**
     * Get lunar date for a solar date
     */
    fun getSolarToLunarDate(day: Int, month: Int, year: Int): Triple<Int, Int, Int> {
        return LunarCalendarConverter.solarToLunar(day, month, year)
    }

    /**
     * Get solar date for a lunar date
     */
    fun getLunarToSolarDate(
        day: Int,
        month: Int,
        year: Int,
        isLeapMonth: Boolean = false
    ): Triple<Int, Int, Int> {
        return LunarCalendarConverter.lunarToSolar(day, month, year, isLeapMonth)
    }

    /**
     * Check if a date is a holiday
     */
    suspend fun isHoliday(day: Int, month: Int): Boolean {
        return repository.hasHolidayOnDate(month, day)
    }

    /**
     * Get all recurring holidays (repeats every year)
     */
    fun getRecurringHolidays() {
        viewModelScope.launch {
            try {
                val holidays = repository.getRecurringHolidays()
                _holidaysForSelectedDate.value = holidays
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch recurring holidays: ${e.message}"
            }
        }
    }

    /**
     * Delete all custom holidays
     */
    fun deleteAllCustomHolidays() {
        viewModelScope.launch {
            try {
                repository.deleteAllCustomHolidays()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete custom holidays: ${e.message}"
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
