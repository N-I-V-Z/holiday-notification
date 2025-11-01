package com.nivz.holidaynotification.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nivz.holidaynotification.R
import com.nivz.holidaynotification.util.calendar.CalendarUtils
import com.nivz.holidaynotification.viewmodel.HolidayViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

/**
 * Fragment showing calendar view with holidays
 */
@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private val viewModel: HolidayViewModel by viewModels()
    private lateinit var calendarGrid: GridLayout
    private lateinit var monthYearDisplay: TextView
    private lateinit var lunarDateDisplay: TextView
    private lateinit var selectedDateHolidays: TextView
    private lateinit var btnPreviousMonth: Button
    private lateinit var btnNextMonth: Button
    private lateinit var btnAddCustomHoliday: Button

    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var selectedDayForCustomHoliday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var selectedMonthForCustomHoliday = currentMonth
    private var selectedYearForCustomHoliday = currentYear

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentMonth", currentMonth)
        outState.putInt("currentYear", currentYear)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarGrid = view.findViewById(R.id.calendarGrid)
        monthYearDisplay = view.findViewById(R.id.monthYearDisplay)
        lunarDateDisplay = view.findViewById(R.id.lunarDateDisplay)
        selectedDateHolidays = view.findViewById(R.id.selectedDateHolidays)
        btnPreviousMonth = view.findViewById(R.id.btnPreviousMonth)
        btnNextMonth = view.findViewById(R.id.btnNextMonth)
        btnAddCustomHoliday = view.findViewById(R.id.btnAddCustomHoliday)

        btnPreviousMonth.setOnClickListener { previousMonth() }
        btnNextMonth.setOnClickListener { nextMonth() }
        btnAddCustomHoliday.setOnClickListener { addCustomHoliday() }

        // Restore saved state if available
        if (savedInstanceState != null) {
            currentMonth = savedInstanceState.getInt("currentMonth", currentMonth)
            currentYear = savedInstanceState.getInt("currentYear", currentYear)
        }

        updateCalendar()

        // Observe all holidays
        viewModel.allHolidays.observe(viewLifecycleOwner) {
            updateCalendar()
        }

        // Observe selected date holidays
        viewModel.holidaysForSelectedDate.observe(viewLifecycleOwner) {
            updateSelectedDateHolidays()
        }
    }

    private fun updateCalendar() {
        calendarGrid.removeAllViews()
        monthYearDisplay.text = "Tháng $currentMonth, $currentYear"

        // Display lunar calendar for first day of month
        val lunarDate = viewModel.getSolarToLunarDate(1, currentMonth, currentYear)
        lunarDateDisplay.text = "(Âm lịch: ${lunarDate.first}/${lunarDate.second}/${lunarDate.third})"

        val daysInMonth = CalendarUtils.getDaysInMonth(currentMonth, currentYear)
        val firstDay = CalendarUtils.getFirstDayOfMonth(currentMonth, currentYear)
        val holidays = viewModel.allHolidays.value ?: emptyList()

        // Add empty cells for days before month starts
        for (i in 1 until firstDay) {
            val emptyView = createEmptyDayView()
            calendarGrid.addView(emptyView)
        }

        // Add day cells
        for (day in 1..daysInMonth) {
            val hasHoliday = holidays.any { it.day == day && it.month == currentMonth }
            val dayView = createDayView(day, hasHoliday)
            calendarGrid.addView(dayView)
        }
    }

    private fun createDayView(day: Int, hasHoliday: Boolean = false): View {
        val container = android.widget.LinearLayout(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 100
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            if (hasHoliday) {
                setBackgroundResource(R.drawable.calendar_day_with_holiday)
            } else {
                setBackgroundResource(R.drawable.calendar_day_cell)
            }
            setPadding(8, 8, 8, 8)
        }

        // Solar calendar (larger text)
        val solarText = android.widget.TextView(requireContext()).apply {
            text = day.toString()
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setTextColor(android.graphics.Color.parseColor("#2C3E50"))
        }

        container.addView(solarText)

        // Lunar calendar (smaller text) - check if lunar calendar display is enabled
        val prefs = requireContext().getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
        val showLunarCalendar = prefs.getBoolean("lunar_calendar_visible", true)

        if (showLunarCalendar) {
            val lunarDate = viewModel.getSolarToLunarDate(day, currentMonth, currentYear)
            val lunarText = android.widget.TextView(requireContext()).apply {
                text = "${lunarDate.first}/${lunarDate.second}"
                textSize = 8f
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setTextColor(android.graphics.Color.parseColor("#95A5A6"))
                setTypeface(null, android.graphics.Typeface.NORMAL)
            }
            container.addView(lunarText)
        }

        val dayView = container
        dayView.apply {

            setOnClickListener {
                selectedDayForCustomHoliday = day
                selectedMonthForCustomHoliday = currentMonth
                selectedYearForCustomHoliday = currentYear

                // Reset all day views
                val daysInMonth = CalendarUtils.getDaysInMonth(currentMonth, currentYear)
                val firstDay = CalendarUtils.getFirstDayOfMonth(currentMonth, currentYear)
                val holidays = viewModel.allHolidays.value ?: emptyList()

                calendarGrid.removeAllViews()

                for (i in 1 until firstDay) {
                    calendarGrid.addView(createEmptyDayView())
                }

                for (d in 1..daysInMonth) {
                    val holidayOnDay = holidays.any { it.day == d && it.month == currentMonth }
                    val view = if (d == day) createSelectedDayView(d, holidayOnDay) else createDayView(d, holidayOnDay)
                    calendarGrid.addView(view)
                }

                // Observer will automatically call updateSelectedDateHolidays()
                viewModel.selectDate(day, currentMonth, currentYear)
            }
        }

        return dayView
    }

    private fun createSelectedDayView(day: Int, hasHoliday: Boolean = false): View {
        val container = android.widget.LinearLayout(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 100
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setBackgroundResource(R.drawable.calendar_day_selected)
            setPadding(8, 8, 8, 8)
        }

        // Solar calendar (larger text)
        val solarText = android.widget.TextView(requireContext()).apply {
            text = day.toString()
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setTextColor(android.graphics.Color.WHITE)
        }

        container.addView(solarText)

        // Lunar calendar (smaller text) - check if lunar calendar display is enabled
        val prefs = requireContext().getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
        val showLunarCalendar = prefs.getBoolean("lunar_calendar_visible", true)

        if (showLunarCalendar) {
            val lunarDate = viewModel.getSolarToLunarDate(day, currentMonth, currentYear)
            val lunarText = android.widget.TextView(requireContext()).apply {
                text = "${lunarDate.first}/${lunarDate.second}"
                textSize = 8f
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setTextColor(android.graphics.Color.parseColor("#FFE0B2"))
                setTypeface(null, android.graphics.Typeface.NORMAL)
            }
            container.addView(lunarText)
        }

        val dayView = container
        dayView.apply {
            setOnClickListener {
                // Observer will automatically call updateSelectedDateHolidays()
                viewModel.selectDate(day, currentMonth, currentYear)
            }
        }

        return dayView
    }

    private fun createEmptyDayView(): View {
        return TextView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 100
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
        }
    }

    private fun updateSelectedDateHolidays() {
        var holidays = viewModel.holidaysForSelectedDate.value ?: emptyList()

        // Remove duplicate holidays (same day/month/lunar/name)
        holidays = holidays.distinctBy { "${it.day}/${it.month}/${it.isLunar}/${it.name}" }

        if (holidays.isEmpty()) {
            selectedDateHolidays.text = "Không có ngày lễ hôm nay"
        } else {
            selectedDateHolidays.text = holidays.joinToString("\n\n") { holiday ->
                if (holiday.description.isNotEmpty()) {
                    "📅 ${holiday.name}\n${holiday.description}"
                } else {
                    "📅 ${holiday.name}"
                }
            }
        }
    }

    private fun previousMonth() {
        val (month, year) = CalendarUtils.getPreviousMonth(currentMonth, currentYear)
        currentMonth = month
        currentYear = year
        updateCalendar()
    }

    private fun nextMonth() {
        val (month, year) = CalendarUtils.getNextMonth(currentMonth, currentYear)
        currentMonth = month
        currentYear = year
        updateCalendar()
    }

    private fun addCustomHoliday() {
        // Ensure a day is selected
        if (selectedDayForCustomHoliday == 0) {
            Toast.makeText(requireContext(), "Vui lòng chọn một ngày trước", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_holiday, null)

        val editName = dialogView.findViewById<android.widget.EditText>(R.id.editHolidayName)
        val editDay = dialogView.findViewById<android.widget.EditText>(R.id.editDay)
        val editMonth = dialogView.findViewById<android.widget.EditText>(R.id.editMonth)
        val editDescription = dialogView.findViewById<android.widget.EditText>(R.id.editDescription)

        // Pre-fill with selected date
        editDay.setText(selectedDayForCustomHoliday.toString())
        editMonth.setText(selectedMonthForCustomHoliday.toString())

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = editName.text.toString().trim()
            val day = editDay.text.toString().toIntOrNull() ?: return@setOnClickListener
            val month = editMonth.text.toString().toIntOrNull() ?: return@setOnClickListener
            val description = editDescription.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên ngày lễ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (day < 1 || day > 31 || month < 1 || month > 12) {
                Toast.makeText(requireContext(), "Ngày tháng không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create custom holiday
            val customHoliday = com.nivz.holidaynotification.data.model.Holiday(
                id = 0,
                name = name,
                description = description,
                day = day,
                month = month,
                year = selectedYearForCustomHoliday,
                isLunar = false,
                isCustom = true,
                colorCode = "#4CAF50",
                notificationEnabled = true,
                notificationDays = 1,
                notificationHour = 9,
                notificationMinute = 0
            )

            viewModel.addHoliday(customHoliday)
            dialog.dismiss()

            Toast.makeText(
                requireContext(),
                "Đã thêm ngày lễ: $name",
                Toast.LENGTH_SHORT
            ).show()

            updateCalendar()
        }

        dialog.show()
    }
}
