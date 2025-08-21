package com.example.todo_android.presentation.ui.tasks.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_android.R
import com.example.todo_android.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 태스크 목록을 RecyclerView에 표시하기 위한 어댑터
 */
class TasksAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskCheckedChange: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        private val checkboxCompleted: CheckBox = itemView.findViewById(R.id.checkboxCompleted)
        private val ivPriority: ImageView = itemView.findViewById(R.id.ivPriority)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTaskClick(getItem(position))
                }
            }

            checkboxCompleted.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTaskCheckedChange(getItem(position))
                }
            }

            btnDelete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTaskDelete(getItem(position))
                }
            }
        }

        fun bind(task: Task) {
            tvTitle.text = task.title
            checkboxCompleted.isChecked = task.isCompleted

            // 완료된 태스크는 취소선 표시
            if (task.isCompleted) {
                tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // 태스크 설명이 있으면 표시
            if (task.description.isNotBlank()) {
                tvDescription.text = task.description
                tvDescription.visibility = View.VISIBLE
            } else {
                tvDescription.visibility = View.GONE
            }

            // 마감일이 있으면 표시
            if (task.dueDate != null) {
                tvDueDate.text = formatDate(task.dueDate)
                tvDueDate.visibility = View.VISIBLE

                // 기한이 지났는지 확인
                if (task.isOverdue()) {
                    tvDueDate.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorError))
                } else {
                    tvDueDate.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorOnSurface))
                }
            } else {
                tvDueDate.visibility = View.GONE
            }

            // 우선순위에 따른 색상 적용
            val priorityColor = when (task.priority) {
                Task.Priority.HIGH -> R.color.colorPriorityHigh
                Task.Priority.MEDIUM -> R.color.colorPriorityMedium
                Task.Priority.LOW -> R.color.colorPriorityLow
            }
            ivPriority.setColorFilter(ContextCompat.getColor(itemView.context, priorityColor))
        }

        private fun formatDate(date: Date): String {
            val format = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            return format.format(date)
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
