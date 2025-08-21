package com.example.todo_android.presentation.ui.tasks.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todo_android.R
import com.example.todo_android.domain.model.Task
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 새 태스크를 추가하는 Fragment
 */
@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private val viewModel: AddTaskViewModel by viewModels()
    
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var spinnerPriority: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)
        
        etTitle = view.findViewById(R.id.etTitle)
        etDescription = view.findViewById(R.id.etDescription)
        btnSelectDate = view.findViewById(R.id.btnSelectDate)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)
        spinnerPriority = view.findViewById(R.id.spinnerPriority)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupPrioritySpinner()
        setupListeners()
        observeViewModel()
    }

    private fun setupPrioritySpinner() {
        val priorities = arrayOf("Low", "Medium", "High")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority.adapter = adapter
        spinnerPriority.setSelection(1) // Medium as default
    }

    private fun setupListeners() {
        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            saveTask()
        }

        btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                updateSelectedDateText()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateSelectedDateText() {
        if (selectedDate != null) {
            val format = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            tvSelectedDate.text = "Selected: ${format.format(selectedDate!!)}"
            tvSelectedDate.visibility = View.VISIBLE
        } else {
            tvSelectedDate.visibility = View.GONE
        }
    }

    private fun saveTask() {
        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            etTitle.error = "Title is required"
            return
        }

        val description = etDescription.text.toString().trim()
        val priority = when (spinnerPriority.selectedItemPosition) {
            0 -> Task.Priority.LOW
            1 -> Task.Priority.MEDIUM
            2 -> Task.Priority.HIGH
            else -> Task.Priority.MEDIUM
        }

        viewModel.saveTask(title, description, selectedDate, priority)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            btnSave.isEnabled = !isLoading
            btnSave.text = if (isLoading) "Saving..." else "Save"
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Task saved successfully!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Failed to save task", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
