package com.productive.taskly.domain.repository

import com.productive.taskly.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class TaskRepositoryTest {

    private lateinit var mockRepository: TaskRepository
    private val task1 = Task(
        id = 1,
        title = "Implement Domain Layer",
        description = "Create entity and repository interfaces",
        priority = Task.Priority.HIGH
    )
    private val task2 = Task(
        id = 2,
        title = "Implement Data Layer",
        description = "Create Room database and repository implementations",
        priority = Task.Priority.MEDIUM
    )
    private val taskList = listOf(task1, task2)

    @Before
    fun setup() {
        mockRepository = mock(TaskRepository::class.java)
    }

    @Test
    fun `Given repository has tasks, When getAllTasks is called, Then it should return all tasks`(): Unit = runBlocking {
        // Given
        val flowList: Flow<List<Task>> = flowOf(taskList)
        `when`(mockRepository.getAllTasks()).thenReturn(flowList)

        // When
        val result = mockRepository.getAllTasks().first()

        // Then
        assertEquals(taskList, result)
        verify(mockRepository).getAllTasks()
    }

    @Test
    fun `Given repository has a task with specified id, When getTaskById is called, Then it should return that task`(): Unit = runBlocking {
        // Given
        `when`(mockRepository.getTaskById(1)).thenReturn(task1)

        // When
        val result = mockRepository.getTaskById(1)

        // Then
        assertEquals(task1, result)
        verify(mockRepository).getTaskById(1)
    }

    @Test
    fun `Given repository does not have a task with specified id, When getTaskById is called, Then it should return null`(): Unit = runBlocking {
        // Given
        `when`(mockRepository.getTaskById(999)).thenReturn(null)

        // When
        val result = mockRepository.getTaskById(999)

        // Then
        assertNull(result)
        verify(mockRepository).getTaskById(999)
    }

    @Test
    fun `When saveTask is called with a new task, Then it should return the task with an assigned id`(): Unit = runBlocking {
        // Given
        val newTask = Task(
            id = 0, // 새 태스크는 id=0으로 표시
            title = "Create UI Layer",
            description = "Implement MVVM pattern with Fragments and ViewModels"
        )
        val savedTask = newTask.copy(id = 3) // 저장 후에는 ID가 할당됨
        `when`(mockRepository.saveTask(newTask)).thenReturn(savedTask)

        // When
        val result = mockRepository.saveTask(newTask)

        // Then
        assertEquals(3, result.id)
        verify(mockRepository).saveTask(newTask)
    }

    @Test
    fun `When updateTask is called, Then it should return the updated task`(): Unit = runBlocking {
        // Given
        val updatedTask = task1.updateTitle("Implement Domain Layer with TDD")
        `when`(mockRepository.updateTask(updatedTask)).thenReturn(updatedTask)

        // When
        val result = mockRepository.updateTask(updatedTask)

        // Then
        assertEquals(updatedTask.title, result.title)
        verify(mockRepository).updateTask(updatedTask)
    }

    @Test
    fun `When deleteTask is called, Then it should complete without errors`() = runBlocking {
        // When
        mockRepository.deleteTask(task1)

        // Then
        verify(mockRepository).deleteTask(task1)
    }

    @Test
    fun `When deleteAllTasks is called, Then it should complete without errors`() = runBlocking {
        // When
        mockRepository.deleteAllTasks()

        // Then
        verify(mockRepository).deleteAllTasks()
    }
}