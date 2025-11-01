package com.nivz.holidaynotification.util.calendar

import java.util.Calendar

/**
 * Utility functions for calendar operations
 * Handles date formatting, comparison, and calculations
 */
object CalendarUtils {

    /**
     * Get days in a month
     */
    fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 0
        }
    }

    /**
     * Check if a year is a leap year
     */
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * Get first day of month (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
     */
    fun getFirstDayOfMonth(month: Int, year: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * Get current date as Triple(day, month, year)
     */
    fun getCurrentDate(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return Triple(day, month, year)
    }

    /**
     * Format date as "DD/MM/YYYY"
     */
    fun formatDate(day: Int, month: Int, year: Int): String {
        return String.format("%02d/%02d/%04d", day, month, year)
    }

    /**
     * Format date as "Monday, 01 January 2024"
     */
    fun formatLongDate(day: Int, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)

        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, java.util.Locale.getDefault())
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault())

        return "$dayOfWeek, %02d $monthName $year".format(day)
    }

    /**
     * Check if two dates are the same
     */
    fun isSameDate(day1: Int, month1: Int, year1: Int, day2: Int, month2: Int, year2: Int): Boolean {
        return day1 == day2 && month1 == month2 && year1 == year2
    }

    /**
     * Get number of days between two dates
     */
    fun getDaysBetween(day1: Int, month1: Int, year1: Int, day2: Int, month2: Int, year2: Int): Int {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()

        cal1.set(year1, month1 - 1, day1)
        cal2.set(year2, month2 - 1, day2)

        return ((cal2.timeInMillis - cal1.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
    }

    /**
     * Get day name (Monday, Tuesday, etc.)
     */
    fun getDayName(day: Int, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, java.util.Locale.getDefault()) ?: ""
    }

    /**
     * Get month name
     */
    fun getMonthName(month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()) ?: ""
    }

    /**
     * Get next month (day, month, year)
     */
    fun getNextMonth(month: Int, year: Int): Pair<Int, Int> {
        return if (month == 12) {
            Pair(1, year + 1)
        } else {
            Pair(month + 1, year)
        }
    }

    /**
     * Get previous month (day, month, year)
     */
    fun getPreviousMonth(month: Int, year: Int): Pair<Int, Int> {
        return if (month == 1) {
            Pair(12, year - 1)
        } else {
            Pair(month - 1, year)
        }
    }

    /**
     * Get Vietnamese month name
     */
    fun getVietnameseMonthName(month: Int): String {
        return when (month) {
            1 -> "Tháng Một"
            2 -> "Tháng Hai"
            3 -> "Tháng Ba"
            4 -> "Tháng Tư"
            5 -> "Tháng Năm"
            6 -> "Tháng Sáu"
            7 -> "Tháng Bảy"
            8 -> "Tháng Tám"
            9 -> "Tháng Chín"
            10 -> "Tháng Mười"
            11 -> "Tháng Mười Một"
            12 -> "Tháng Mười Hai"
            else -> ""
        }
    }
}
