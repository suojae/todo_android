package com.example.todo_android.domain.usecase

import com.example.todo_android.domain.model.Task
import com.example.todo_android.domain.repository.TaskRepository

import javax.inject.Inject

/**
 * 태스크를 업데이트하는 유스케이스
 */
class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * 유스케이스 실행
     *
     * @param task 업데이트할 태스크
     * @return 업데이트된 태스크
     * @throws IllegalArgumentException 제목이 비어있을 경우
     */
    suspend operator fun invoke(task: Task): Task {
        // 유효성 검사
        if (task.title.isBlank()) {
            throw IllegalArgumentException("Task title cannot be empty")
        }

        // 태스크 업데이트 및 반환
        return taskRepository.updateTask(task.copy(modifiedAt = java.util.Date()))
    }
}
