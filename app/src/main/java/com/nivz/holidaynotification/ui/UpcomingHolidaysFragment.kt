package com.nivz.holidaynotification.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nivz.holidaynotification.R
import com.nivz.holidaynotification.data.model.Holiday
import com.nivz.holidaynotification.viewmodel.HolidayViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment showing upcoming holidays
 */
@AndroidEntryPoint
class UpcomingHolidaysFragment : Fragment() {

    private val viewModel: HolidayViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HolidayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming_holidays, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.upcomingHolidaysRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HolidayListAdapter(emptyList())
        recyclerView.adapter = adapter

        // Observe holidays
        viewModel.allHolidays.observe(viewLifecycleOwner) { holidays ->
            if (holidays != null) {
                // Filter and sort upcoming holidays
                val upcoming = getUpcomingHolidays(holidays)
                adapter.updateHolidays(upcoming)
            }
        }
    }

    private fun getUpcomingHolidays(holidays: List<Holiday>): List<Holiday> {
        // Sort by month then day (numeric sort, not string sort)
        return holidays
            .filter { !it.isLunar }  // For now, show only solar holidays
            .sortedWith(compareBy({ it.month }, { it.day }))
            .take(10)  // Show only next 10 holidays
    }
}

/**
 * RecyclerView adapter for holiday list
 */
class HolidayListAdapter(private var holidays: List<Holiday>) :
    RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_holiday, parent, false)
        return HolidayViewHolder(view)
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        holder.bind(holidays[position])
    }

    override fun getItemCount(): Int = holidays.size

    fun updateHolidays(newHolidays: List<Holiday>) {
        holidays = newHolidays
        notifyDataSetChanged()
    }

    class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(holiday: Holiday) {
            val nameView = itemView.findViewById<android.widget.TextView>(R.id.holidayName)
            val dateView = itemView.findViewById<android.widget.TextView>(R.id.holidayDate)
            val descView = itemView.findViewById<android.widget.TextView>(R.id.holidayDesc)
            val colorView = itemView.findViewById<View>(R.id.colorIndicator)

            nameView.text = holiday.name
            dateView.text = "${holiday.day}/${holiday.month}"
            descView.text = holiday.description
            colorView.setBackgroundColor(android.graphics.Color.parseColor(holiday.colorCode))
        }
    }
}
