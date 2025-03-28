package com.productive.taskly.domain.repository

import com.productive.taskly.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    /**
     * 모든 태스크를 가져옵니다.
     *
     * @return 태스크 리스트가 포함된 Flow
     */
    fun getAllTasks(): Flow<List<Task>>

    /**
     * 특정 ID의 태스크를 가져옵니다.
     *
     * @param id 태스크 ID
     * @return 찾은 태스크 또는 없을 경우 null
     */
    suspend fun getTaskById(id: Long): Task?

    /**
     * 완료된 태스크만 가져옵니다.
     *
     * @return 완료된 태스크 리스트가 포함된 Flow
     */
    fun getCompletedTasks(): Flow<List<Task>>

    /**
     * 미완료된 태스크만 가져옵니다.
     *
     * @return 미완료된 태스크 리스트가 포함된 Flow
     */
    fun getActiveTasks(): Flow<List<Task>>

    /**
     * 태스크를 저장합니다.
     *
     * @param task 저장할 태스크
     * @return 저장된 태스크 (ID가 할당된 상태)
     */
    suspend fun saveTask(task: Task): Task

    /**
     * 태스크를 업데이트합니다.
     *
     * @param task 업데이트할 태스크
     * @return 업데이트된 태스크
     */
    suspend fun updateTask(task: Task): Task

    /**
     * 태스크를 삭제합니다.
     *
     * @param task 삭제할 태스크
     */
    suspend fun deleteTask(task: Task)

    /**
     * 모든 태스크를 삭제합니다.
     */
    suspend fun deleteAllTasks()

    /**
     * 완료된 태스크만 삭제합니다.
     */
    suspend fun deleteCompletedTasks()
}