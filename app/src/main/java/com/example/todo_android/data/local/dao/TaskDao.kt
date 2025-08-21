package com.example.todo_android.data.local.dao

import androidx.room.*
import com.example.todo_android.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * 태스크 데이터 접근 객체 (DAO)
 */
@Dao
interface TaskDao {

    /**
     * 모든 태스크 조회
     */
    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    /**
     * 특정 ID의 태스크 조회
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    /**
     * 완료된 태스크만 조회
     */
    @Query("SELECT * FROM tasks WHERE is_completed = 1 ORDER BY created_at DESC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    /**
     * 미완료 태스크만 조회
     */
    @Query("SELECT * FROM tasks WHERE is_completed = 0 ORDER BY created_at DESC")
    fun getActiveTasks(): Flow<List<TaskEntity>>

    /**
     * 태스크 삽입
     */
    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    /**
     * 태스크 업데이트
     */
    @Update
    suspend fun updateTask(task: TaskEntity)

    /**
     * 태스크 삭제
     */
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    /**
     * 모든 태스크 삭제
     */
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    /**
     * 완료된 태스크만 삭제
     */
    @Query("DELETE FROM tasks WHERE is_completed = 1")
    suspend fun deleteCompletedTasks()
}
