package com.productive.taskly.domain.usecase

import com.productive.taskly.domain.model.Task
import com.productive.taskly.domain.repository.TaskRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.Date

class AddTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var addTaskUseCase: AddTaskUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        addTaskUseCase = AddTaskUseCase(taskRepository)
    }

    @Test
    fun `Given valid task parameters, When adding a task, Then return saved task with ID`() = runBlocking {
        // Given
        val title = "Implement Presentation Layer"
        val description = "Create MVVM architecture with Fragments and ViewModels"
        val dueDate = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000) // 일주일 후
        val priority = Task.Priority.HIGH

        val expectedTask = Task(
            id = 0, // 아직 저장 전이므로 ID는 0
            title = title,
            description = description,
            dueDate = dueDate,
            priority = priority
        )

        val savedTask = expectedTask.copy(id = 42) // 저장 후에는 ID가 할당됨

        `when`(taskRepository.saveTask(org.mockito.ArgumentMatchers.any()))
            .thenReturn(savedTask)

        // When
        val result = addTaskUseCase(title, description, dueDate, priority)

        // Then
        assertEquals(42, result.id)
        assertEquals(title, result.title)
        assertEquals(description, result.description)
        assertEquals(dueDate, result.dueDate)
        assertEquals(priority, result.priority)
        assertEquals(false, result.isCompleted)

        // saveTask가 호출되었는지 확인
        verify(taskRepository).saveTask(org.mockito.ArgumentMatchers.any())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given empty title, When adding a task, Then throw IllegalArgumentException`() = runBlocking {
        // Given
        val title = ""
        val description = "Some description"

        // When
        addTaskUseCase(title, description) // 예외 발생 예상

        // Then: expected = IllegalArgumentException::class
    }

    @Test
    fun `Given only title parameter, When adding a task, Then add task with default values`() = runBlocking {
        // Given
        val title = "Quick Task"
        val expectedTask = Task(
            id = 0,
            title = title
        )
        val savedTask = expectedTask.copy(id = 5)

        `when`(taskRepository.saveTask(org.mockito.ArgumentMatchers.any()))
            .thenReturn(savedTask)

        // When
        val result = addTaskUseCase(title)

        // Then
        assertEquals(5, result.id)
        assertEquals(title, result.title)
        assertEquals("", result.description) // 기본값
        assertEquals(null, result.dueDate) // 기본값
        assertEquals(Task.Priority.MEDIUM, result.priority) // 기본값
        assertEquals(false, result.isCompleted) // 기본값

        verify(taskRepository).saveTask(org.mockito.ArgumentMatchers.any())
    }
}