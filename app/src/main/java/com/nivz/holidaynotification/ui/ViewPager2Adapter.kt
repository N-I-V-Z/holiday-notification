package com.nivz.holidaynotification.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter for ViewPager2 with 4 tabs
 */
class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingHolidaysFragment()
            1 -> CalendarFragment()
            2 -> ManageHolidaysFragment()
            3 -> SettingsFragment()
            else -> UpcomingHolidaysFragment()
        }
    }

    fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> "Sắp Tới"
            1 -> "Lịch"
            2 -> "Quản Lý"
            3 -> "Cài Đặt"
            else -> ""
        }
    }
}
