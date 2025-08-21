package com.example.todo_android.domain.model

import java.util.Date

/**
 * 할일(Task) 도메인 모델
 *
 * 불변(immutable) 클래스로 설계하여 상태 변경 시 새 인스턴스를 반환합니다.
 */
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDate: Date? = null,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val createdAt: Date = Date(),
    val modifiedAt: Date = Date()
) {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    /**
     * 태스크를 완료 상태로 변경
     *
     * @return 완료 상태가 된 새 Task 인스턴스
     */
    fun markAsCompleted(): Task = copy(
        isCompleted = true,
        modifiedAt = Date()
    )

    /**
     * 태스크를 미완료 상태로 변경
     *
     * @return 미완료 상태가 된 새 Task 인스턴스
     */
    fun markAsActive(): Task = copy(
        isCompleted = false,
        modifiedAt = Date()
    )

    /**
     * 태스크 제목 업데이트
     *
     * @param title 새 제목
     * @return 제목이 변경된 새 Task 인스턴스
     */
    fun updateTitle(title: String): Task = copy(
        title = title,
        modifiedAt = Date()
    )

    /**
     * 태스크 설명 업데이트
     *
     * @param description 새 설명
     * @return 설명이 변경된 새 Task 인스턴스
     */
    fun updateDescription(description: String): Task = copy(
        description = description,
        modifiedAt = Date()
    )

    /**
     * 태스크 마감일 업데이트
     *
     * @param dueDate 새 마감일
     * @return 마감일이 변경된 새 Task 인스턴스
     */
    fun updateDueDate(dueDate: Date?): Task = copy(
        dueDate = dueDate,
        modifiedAt = Date()
    )

    /**
     * 태스크 우선순위 업데이트
     *
     * @param priority 새 우선순위
     * @return 우선순위가 변경된 새 Task 인스턴스
     */
    fun updatePriority(priority: Priority): Task = copy(
        priority = priority,
        modifiedAt = Date()
    )

    /**
     * 태스크가 마감일이 지났는지 확인
     *
     * @return 마감일이 지났으면 true, 아니면 false
     */
    fun isOverdue(): Boolean {
        return !isCompleted && dueDate != null && Date().after(dueDate)
    }
}