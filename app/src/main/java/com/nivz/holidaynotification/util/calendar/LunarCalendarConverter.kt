package com.nivz.holidaynotification.util.calendar

import java.util.Calendar

/**
 * Lunar Calendar Converter
 * Converts between Gregorian (Solar) and Lunar calendar systems
 * Using Vietnamese lunar calendar algorithm
 */
object LunarCalendarConverter {

    private const val SYNODIC_MONTH = 29.530588861  // Average lunar month length
    private const val JD2000 = 2451545.0            // Julian Day for Jan 1, 2000

    /**
     * Convert solar date to lunar date
     * @param solarDay Day of month (1-31)
     * @param solarMonth Month (1-12)
     * @param solarYear Year
     * @return Triple of (lunarDay, lunarMonth, lunarYear)
     */
    fun solarToLunar(solarDay: Int, solarMonth: Int, solarYear: Int): Triple<Int, Int, Int> {
        val jd = calcJulianDay(solarDay, solarMonth, solarYear)
        return getlunarFromJd(jd)
    }

    /**
     * Convert lunar date to solar date
     * @param lunarDay Day of month (1-30)
     * @param lunarMonth Month (1-13)
     * @param lunarYear Year
     * @param isLeapMonth Whether this is a leap month
     * @return Triple of (solarDay, solarMonth, solarYear)
     */
    fun lunarToSolar(
        lunarDay: Int,
        lunarMonth: Int,
        lunarYear: Int,
        isLeapMonth: Boolean = false
    ): Triple<Int, Int, Int> {
        val jd = getSolarJdFromLunar(lunarDay, lunarMonth, lunarYear, isLeapMonth)
        return getGregorianFromJd(jd)
    }

    /**
     * Calculate Julian Day Number from a Gregorian date
     */
    private fun calcJulianDay(day: Int, month: Int, year: Int): Double {
        var m = month
        var y = year

        if (m <= 2) {
            y -= 1
            m += 12
        }

        val a = y / 100
        val b = 2 - a + a / 4

        return kotlin.math.floor(365.25 * (y + 4716)) +
                kotlin.math.floor(30.6001 * (m + 1)) +
                day + b - 1524.5
    }

    /**
     * Get lunar date from Julian Day Number
     * Strategy: Find lunar year start, then count months from there
     */
    private fun getlunarFromJd(jd: Double): Triple<Int, Int, Int> {
        val (gDay, gMonth, gYear) = getGregorianFromJd(jd)

        // Step 1: Find lunar day from most recent new moon
        val k = kotlin.math.floor((jd - 2451550.1) / 29.530588861)
        var jd2 = getNewMoonDay(k)

        if (jd < jd2) {
            jd2 = getNewMoonDay(k - 1)
        }

        val lunarDay = (jd - jd2 + 1).toInt().coerceIn(1, 30)

        // Step 2: Find lunar year by finding the lunar new year (month 1)
        // Lunar New Year (TET) is usually late Jan/early Feb
        // Formula: k = (year - 2000 + 0.5) * 12.36875 gives approximate k for lunar new year

        var lunarYear = gYear
        var k1 = kotlin.math.floor((gYear - 2000 + 0.5) * 12.36875)
        var jd1 = getNewMoonDay(k1)

        // If current date is before this year's new moon, use previous year
        if (jd < jd1) {
            lunarYear = gYear - 1
            k1 = kotlin.math.floor((lunarYear - 2000 + 0.5) * 12.36875)
            jd1 = getNewMoonDay(k1)
        }

        // Step 3: Count which month we're in, starting from new year
        var lunarMonth = 1
        var currentK = k1

        // Check up to 13 months (some lunar years have 13 months - leap month)
        for (month in 1..13) {
            val monthStartJd = getNewMoonDay(currentK)
            val nextMonthJd = getNewMoonDay(currentK + 1)

            if (jd >= monthStartJd && jd < nextMonthJd) {
                lunarMonth = month
                break
            }
            currentK += 1
        }

        return Triple(lunarDay, lunarMonth, lunarYear)
    }

    /**
     * Get Gregorian date from Julian Day Number
     */
    private fun getGregorianFromJd(jd: Double): Triple<Int, Int, Int> {
        var z = kotlin.math.floor(jd + 0.5).toLong()
        val a = if (z < 2299161) z else {
            val alpha = kotlin.math.floor((z - 1867216.25) / 36524.25).toLong()
            z + 1 + alpha - kotlin.math.floor(alpha.toDouble() / 4).toLong()
        }

        val b = a + 1524
        val c = kotlin.math.floor((b.toDouble() - 122.1) / 365.25).toLong()
        val d = kotlin.math.floor(365.25 * c).toLong()
        val e = kotlin.math.floor((b.toDouble() - d) / 30.6001).toLong()

        val day = (b.toInt() - d.toInt() - kotlin.math.floor(30.6001 * e).toInt())
        val month = if (e < 14) (e - 1).toInt() else (e - 13).toInt()
        val year = if (month > 2) (c - 4716).toInt() else (c - 4715).toInt()

        return Triple(day, month, year)
    }

    /**
     * Calculate the Julian Day Number of the New Moon closest to a given day k
     * k is the number of new moons since 2000.0
     */
    private fun getNewMoonDay(k: Double): Double {
        var t = k / 1236.85
        var jd = 2451550.09766 + 29.530588861 * k
        jd += 0.00915 * sinDeg(178.18102 + 360.9465571 * t)
        jd += -0.00383 * sinDeg(355.30226 + 359.0966543 * t)
        jd += 0.00226 * sinDeg(234.95742 + 19.0860920 * t)
        jd += 0.00154 * sinDeg(352.91309 + 328.4545314 * t)
        jd += 0.00125 * sinDeg(325.71528 + 12.9590088 * t)
        jd += 0.00110 * sinDeg(155.71993 + 331.2391741 * t)
        jd += 0.00098 * sinDeg(34.52410 + 4.8694955 * t)
        jd += 0.00047 * sinDeg(232.35108 + 19.3744470 * t)
        jd += 0.00035 * sinDeg(235.59665 + 7.4606679 * t)
        jd += 0.00030 * sinDeg(267.20768 + 331.5766309 * t)
        jd += 0.00027 * sinDeg(290.27129 + 4.7581314 * t)
        jd += 0.00023 * sinDeg(21.02754 + 628.5537314 * t)
        jd += 0.00020 * sinDeg(162.40698 + 715.2000061 * t)
        jd += 0.00019 * sinDeg(203.00191 + 1.9160119 * t)
        jd += 0.00017 * sinDeg(234.27809 + 7.4592200 * t)
        jd += 0.00014 * sinDeg(46.59303 + 11.8408888 * t)
        jd += 0.00014 * sinDeg(99.30718 + 4533.4294782 * t)
        jd += 0.00013 * sinDeg(193.35053 + 64.8042952 * t)
        jd += 0.00012 * sinDeg(252.17436 + 149.4812441 * t)
        jd += 0.00012 * sinDeg(45.16034 + -67.5246091 * t)
        jd += 0.00011 * sinDeg(296.72796 + 9.9692612 * t)

        return jd
    }

    /**
     * Check if a month is a leap month in lunar calendar
     */
    private fun isLeapMonth(jd1: Double, jd2: Double): Boolean {
        return kotlin.math.floor((jd2 + 1) - jd1 + 0.5) > 29.9
    }

    /**
     * Calculate solar JD from lunar date
     */
    private fun getSolarJdFromLunar(
        lunarDay: Int,
        lunarMonth: Int,
        lunarYear: Int,
        isLeapMonth: Boolean = false
    ): Double {
        val k = kotlin.math.floor((lunarYear + ((lunarMonth.toDouble() - 1) / 12.0)) * 12.36875).toDouble()
        var jd1 = getNewMoonDay(k)
        var jd2 = getNewMoonDay(k + 1)

        if (isLeapMonth) {
            jd2 = getNewMoonDay(k + 2)
        }

        return jd1 + (lunarDay - 1)
    }

    /**
     * Helper: sine function with degrees
     */
    private fun sinDeg(deg: Double): Double {
        return kotlin.math.sin(Math.toRadians(deg))
    }

    /**
     * Helper: cosine function with degrees
     */
    private fun cosDeg(deg: Double): Double {
        return kotlin.math.cos(Math.toRadians(deg))
    }
}
