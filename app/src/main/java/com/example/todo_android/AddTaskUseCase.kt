package com.productive.taskly.domain.usecase

import com.productive.taskly.domain.model.Task
import com.productive.taskly.domain.repository.TaskRepository
import java.util.Date
import javax.inject.Inject

/**
 * 새 태스크를 추가하는 유스케이스
 */
class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * 유스케이스 실행
     *
     * @param title 태스크 제목
     * @param description 태스크 설명
     * @param dueDate 마감일
     * @param priority 우선순위
     * @return 저장된 태스크 (ID 할당됨)
     * @throws IllegalArgumentException 제목이 비어있을 경우
     */
    suspend operator fun invoke(
        title: String,
        description: String = "",
        dueDate: Date? = null,
        priority: Task.Priority = Task.Priority.MEDIUM
    ): Task {
        // 유효성 검사
        if (title.isBlank()) {
            throw IllegalArgumentException("Task title cannot be empty")
        }

        // 새 태스크 생성
        val newTask = Task(
            id = 0, // 리포지토리에서 저장 시 ID 할당
            title = title,
            description = description,
            dueDate = dueDate,
            isCompleted = false,
            priority = priority,
            createdAt = Date(),
            modifiedAt = Date()
        )

        // 태스크 저장 및 반환
        return taskRepository.saveTask(newTask)
    }
}