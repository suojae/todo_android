package com.example.todo_android.domain.usecase

import com.example.todo_android.domain.model.Task
import com.example.todo_android.domain.repository.TaskRepository

import javax.inject.Inject

/**
 * 태스크를 삭제하는 유스케이스
 */
class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * 특정 태스크 삭제
     */
    suspend operator fun invoke(task: Task) {
        taskRepository.deleteTask(task)
    }

    /**
     * 모든 태스크 삭제
     */
    suspend fun deleteAllTasks() {
        taskRepository.deleteAllTasks()
    }

    /**
     * 완료된 태스크만 삭제
     */
    suspend fun deleteCompletedTasks() {
        taskRepository.deleteCompletedTasks()
    }
}
