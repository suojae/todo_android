package com.example.todo_android.data.repository

import com.example.todo_android.data.local.dao.TaskDao
import com.example.todo_android.data.local.entity.toDomainModel
import com.example.todo_android.data.local.entity.toEntity
import com.example.todo_android.domain.model.Task
import com.example.todo_android.domain.repository.TaskRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TaskRepository의 구현체
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id)?.toDomainModel()
    }

    override fun getCompletedTasks(): Flow<List<Task>> {
        return taskDao.getCompletedTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getActiveTasks(): Flow<List<Task>> {
        return taskDao.getActiveTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveTask(task: Task): Task {
        val taskEntity = task.toEntity()
        val id = taskDao.insertTask(taskEntity)
        return taskEntity.copy(id = id).toDomainModel()
    }

    override suspend fun updateTask(task: Task): Task {
        val taskEntity = task.toEntity()
        taskDao.updateTask(taskEntity)
        return taskEntity.toDomainModel()
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    override suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }
}
