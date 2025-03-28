package com.example.todo_android

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class TaskTest {

    @Test
    fun `When task is created, Then it should have correct properties`() {
        // Given
        val id = 1L
        val title = "Complete TDD Tutorial"
        val description = "Learn and practice Test Driven Development"
        val dueDate = Date()
        val isCompleted = false
        val priority = Task.Priority.HIGH

        // When
        val task = Task(
            id = id,
            title = title,
            description = description,
            dueDate = dueDate,
            isCompleted = isCompleted,
            priority = priority
        )

        // Then
        assertEquals(id, task.id)
        assertEquals(title, task.title)
        assertEquals(description, task.description)
        assertEquals(dueDate, task.dueDate)
        assertEquals(isCompleted, task.isCompleted)
        assertEquals(priority, task.priority)
    }

    @Test
    fun `When markAsCompleted is called, Then task should be completed`() {
        // Given
        val task = Task(
            id = 1L,
            title = "Study Kotlin Coroutines",
            description = "Learn about coroutine scopes and contexts",
            dueDate = Date(),
            isCompleted = false,
            priority = Task.Priority.MEDIUM
        )

        // When
        val completedTask = task.markAsCompleted()

        // Then
        assertTrue(completedTask.isCompleted)
    }

    @Test
    fun `When markAsActive is called, Then task should be active`() {
        // Given
        val task = Task(
            id = 1L,
            title = "Refactor Legacy Code",
            description = "Apply clean code principles",
            dueDate = Date(),
            isCompleted = true,
            priority = Task.Priority.LOW
        )

        // When
        val activeTask = task.markAsActive()

        // Then
        assertFalse(activeTask.isCompleted)
    }

    @Test
    fun `When updateTitle is called, Then task should have new title`() {
        // Given
        val task = Task(
            id = 1L,
            title = "Original Title",
            description = "Some description",
            dueDate = Date(),
            isCompleted = false,
            priority = Task.Priority.MEDIUM
        )
        val newTitle = "Updated Title"

        // When
        val updatedTask = task.updateTitle(newTitle)

        // Then
        assertEquals(newTitle, updatedTask.title)
    }

    @Test
    fun `When updatePriority is called, Then task should have new priority`() {
        // Given
        val task = Task(
            id = 1L,
            title = "Code Review",
            description = "Review PR #42",
            dueDate = Date(),
            isCompleted = false,
            priority = Task.Priority.LOW
        )

        // When
        val updatedTask = task.updatePriority(Task.Priority.HIGH)

        // Then
        assertEquals(Task.Priority.HIGH, updatedTask.priority)
    }
}