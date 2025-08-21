package com.example.todo_android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo_android.domain.model.Task
import java.util.Date

/**
 * Room 데이터베이스용 태스크 엔티티
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "due_date")
    val dueDate: Date? = null,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "priority")
    val priority: Int = 1, // 0: LOW, 1: MEDIUM, 2: HIGH

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Date = Date()
)

/**
 * TaskEntity를 Task 도메인 모델로 변환
 */
fun TaskEntity.toDomainModel(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        priority = when (priority) {
            0 -> Task.Priority.LOW
            1 -> Task.Priority.MEDIUM
            2 -> Task.Priority.HIGH
            else -> Task.Priority.MEDIUM
        },
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

/**
 * Task 도메인 모델을 TaskEntity로 변환
 */
fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        priority = when (priority) {
            Task.Priority.LOW -> 0
            Task.Priority.MEDIUM -> 1
            Task.Priority.HIGH -> 2
        },
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}
