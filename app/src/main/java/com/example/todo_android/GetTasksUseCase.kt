package com.productive.taskly.domain.usecase

import com.productive.taskly.domain.model.Task
import com.productive.taskly.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 태스크 목록을 가져오는 유스케이스
 *
 * 필터 조건에 따라 태스크 목록을 가져오고, 우선순위에 따라 정렬합니다.
 */
class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * 유스케이스 실행
     *
     * @param filter 적용할 필터
     * @return 필터링 및 정렬된 태스크 목록 Flow
     */
    operator fun invoke(filter: Filter = Filter.ALL): Flow<List<Task>> {
        return when (filter) {
            Filter.ALL -> taskRepository.getAllTasks()
            Filter.ACTIVE -> taskRepository.getActiveTasks()
            Filter.COMPLETED -> taskRepository.getCompletedTasks()
            Filter.HIGH_PRIORITY -> taskRepository.getAllTasks()
                .map { tasks -> tasks.filter { it.priority == Task.Priority.HIGH } }
            Filter.MEDIUM_PRIORITY -> taskRepository.getAllTasks()
                .map { tasks -> tasks.filter { it.priority == Task.Priority.MEDIUM } }
            Filter.LOW_PRIORITY -> taskRepository.getAllTasks()
                .map { tasks -> tasks.filter { it.priority == Task.Priority.LOW } }
            Filter.WITH_DEADLINE -> taskRepository.getAllTasks()
                .map { tasks -> tasks.filter { it.dueDate != null } }
            Filter.OVERDUE -> taskRepository.getAllTasks()
                .map { tasks -> tasks.filter { it.isOverdue() } }
        }.map { tasks ->
            // 우선순위 내림차순으로 정렬 (HIGH, MEDIUM, LOW)
            tasks.sortedWith(
                compareByDescending<Task> { it.priority }
                    .thenBy { it.dueDate } // 마감일 오름차순
                    .thenByDescending { it.createdAt } // 생성일 내림차순 (최신순)
            )
        }
    }

    /**
     * 필터 옵션
     */
    enum class Filter {
        ALL,
        ACTIVE,
        COMPLETED,
        HIGH_PRIORITY,
        MEDIUM_PRIORITY,
        LOW_PRIORITY,
        WITH_DEADLINE,
        OVERDUE
    }
}