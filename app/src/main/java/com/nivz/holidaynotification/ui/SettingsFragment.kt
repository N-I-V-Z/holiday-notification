package com.nivz.holidaynotification.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nivz.holidaynotification.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.TimeZone

/**
 * Fragment for app settings
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var switchNotifications: Switch
    private lateinit var switchLunarCalendar: Switch
    private lateinit var notificationHour: EditText
    private lateinit var notificationMinute: EditText
    private lateinit var timezoneDisplay: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchNotifications = view.findViewById(R.id.switchNotifications)
        switchLunarCalendar = view.findViewById(R.id.switchLunarCalendar)
        notificationHour = view.findViewById(R.id.notificationHour)
        notificationMinute = view.findViewById(R.id.notificationMinute)
        timezoneDisplay = view.findViewById(R.id.timezoneDisplay)

        // Load settings from SharedPreferences
        loadSettings()

        // Display system timezone
        val timezone = TimeZone.getDefault()
        val timezoneId = timezone.id
        val offset = timezone.rawOffset / (1000 * 60 * 60)
        timezoneDisplay.text = "$timezoneId (UTC+$offset)"

        // Auto-save when notification toggle changes
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            notificationHour.isEnabled = isChecked
            notificationMinute.isEnabled = isChecked
            saveSettings()
        }

        // Auto-save when lunar calendar toggle changes
        switchLunarCalendar.setOnCheckedChangeListener { _, _ ->
            saveSettings()
        }

        // Auto-save when hour/minute values change
        notificationHour.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) saveSettings()
        }

        notificationMinute.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) saveSettings()
        }
    }

    private fun loadSettings() {
        val prefs = requireContext().getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)

        switchNotifications.isChecked = prefs.getBoolean("notifications_enabled", true)
        switchLunarCalendar.isChecked = prefs.getBoolean("lunar_calendar_visible", true)
        notificationHour.setText(prefs.getInt("notification_hour", 9).toString())
        notificationMinute.setText(prefs.getInt("notification_minute", 0).toString())

        notificationHour.isEnabled = switchNotifications.isChecked
        notificationMinute.isEnabled = switchNotifications.isChecked
    }

    private fun saveSettings() {
        val prefs = requireContext().getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)

        try {
            prefs.edit().apply {
                putBoolean("notifications_enabled", switchNotifications.isChecked)
                putBoolean("lunar_calendar_visible", switchLunarCalendar.isChecked)
                putInt("notification_hour", notificationHour.text.toString().toIntOrNull() ?: 9)
                putInt("notification_minute", notificationMinute.text.toString().toIntOrNull() ?: 0)
                apply()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Lỗi lưu cài đặt", Toast.LENGTH_SHORT).show()
        }
    }
}
