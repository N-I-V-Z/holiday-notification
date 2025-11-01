package com.nivz.holidaynotification.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nivz.holidaynotification.R
import com.nivz.holidaynotification.data.model.Holiday
import com.nivz.holidaynotification.viewmodel.HolidayViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for managing custom holidays
 */
@AndroidEntryPoint
class ManageHolidaysFragment : Fragment() {

    private val viewModel: HolidayViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var adapter: CustomHolidaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_holidays, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.customHolidaysRecycler)
        emptyState = view.findViewById(R.id.emptyState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CustomHolidaysAdapter(
            emptyList(),
            onEdit = { holiday -> editHoliday(holiday) },
            onDelete = { holiday -> deleteHoliday(holiday) }
        )
        recyclerView.adapter = adapter

        // Observe custom holidays
        viewModel.customHolidays.observe(viewLifecycleOwner) { holidays ->
            if (holidays.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateHolidays(holidays)
            }
        }
    }

    private fun editHoliday(holiday: Holiday) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_holiday, null)

        val editName = dialogView.findViewById<EditText>(R.id.editHolidayName)
        val editDay = dialogView.findViewById<EditText>(R.id.editDay)
        val editMonth = dialogView.findViewById<EditText>(R.id.editMonth)
        val editDescription = dialogView.findViewById<EditText>(R.id.editDescription)

        // Pre-fill with holiday data
        editName.setText(holiday.name)
        editDay.setText(holiday.day.toString())
        editMonth.setText(holiday.month.toString())
        editDescription.setText(holiday.description)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<android.widget.Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<android.widget.Button>(R.id.btnSave).setOnClickListener {
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

            val updatedHoliday = holiday.copy(
                name = name,
                description = description,
                day = day,
                month = month
            )

            viewModel.updateHoliday(updatedHoliday)
            dialog.dismiss()

            Toast.makeText(
                requireContext(),
                "Cập nhật: $name",
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog.show()
    }

    private fun deleteHoliday(holiday: Holiday) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa ngày lễ")
            .setMessage("Bạn có chắc chắn muốn xóa \"${holiday.name}\"?")
            .setPositiveButton("Xóa") { _, _ ->
                viewModel.deleteHoliday(holiday)
                Toast.makeText(
                    requireContext(),
                    "Đã xóa: ${holiday.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}

/**
 * RecyclerView adapter for custom holidays
 */
class CustomHolidaysAdapter(
    private var holidays: List<Holiday>,
    private val onEdit: (Holiday) -> Unit,
    private val onDelete: (Holiday) -> Unit
) : RecyclerView.Adapter<CustomHolidaysAdapter.CustomHolidayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolidayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_holiday, parent, false)
        return CustomHolidayViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomHolidayViewHolder, position: Int) {
        holder.bind(holidays[position], onEdit, onDelete)
    }

    override fun getItemCount(): Int = holidays.size

    fun updateHolidays(newHolidays: List<Holiday>) {
        holidays = newHolidays
        notifyDataSetChanged()
    }

    class CustomHolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(holiday: Holiday, onEdit: (Holiday) -> Unit, onDelete: (Holiday) -> Unit) {
            val nameView = itemView.findViewById<android.widget.TextView>(R.id.holidayNameText)
            val dateView = itemView.findViewById<android.widget.TextView>(R.id.holidayDateText)
            val descView = itemView.findViewById<android.widget.TextView>(R.id.holidayDescText)
            val colorView = itemView.findViewById<View>(R.id.colorIndicator)
            val btnEdit = itemView.findViewById<android.widget.Button>(R.id.btnEdit)
            val btnDelete = itemView.findViewById<android.widget.Button>(R.id.btnDelete)

            nameView.text = holiday.name
            dateView.text = "${holiday.day}/${holiday.month}"
            descView.text = holiday.description.ifEmpty { "Không có mô tả" }
            colorView.setBackgroundColor(android.graphics.Color.parseColor(holiday.colorCode))

            btnEdit.setOnClickListener { onEdit(holiday) }
            btnDelete.setOnClickListener { onDelete(holiday) }
        }
    }
}
