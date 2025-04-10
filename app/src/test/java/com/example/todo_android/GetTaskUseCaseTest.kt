import com.productive.taskly.domain.model.Task
import com.productive.taskly.domain.repository.TaskRepository
import com.productive.taskly.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class GetTasksUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksUseCase: GetTasksUseCase

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
    private val task3 = Task(
        id = 3,
        title = "Write Unit Tests",
        description = "Test all components",
        priority = Task.Priority.HIGH,
        isCompleted = true
    )
    private val allTasks = listOf(task1, task2, task3)

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        getTasksUseCase = GetTasksUseCase(taskRepository)
    }

    @Test
    fun `Given there are tasks, When executed with ALL filter, Then return all tasks sorted by priority`(): Unit = runBlocking {
        // Given
        `when`(taskRepository.getAllTasks()).thenReturn(flowOf(allTasks))

        // When
        val result = getTasksUseCase(GetTasksUseCase.Filter.ALL).first()

        // Then
        val expectedOrder = listOf(task1, task3, task2) // HIGH, HIGH, MEDIUM
        assertEquals(expectedOrder.map { it.id }, result.map { it.id })
        verify(taskRepository).getAllTasks()
    }

    @Test
    fun `Given there are active tasks, When executed with ACTIVE filter, Then return only active tasks`(): Unit = runBlocking {
        // Given
        val activeTasks = listOf(task1, task2)
        `when`(taskRepository.getActiveTasks()).thenReturn(flowOf(activeTasks))

        // When
        val result = getTasksUseCase(GetTasksUseCase.Filter.ACTIVE).first()

        // Then
        assertEquals(activeTasks.size, result.size)
        assertEquals(activeTasks.map { it.id }.sorted(), result.map { it.id }.sorted())
        verify(taskRepository).getActiveTasks()
    }

    @Test
    fun `Given there are completed tasks, When executed with COMPLETED filter, Then return only completed tasks`(): Unit = runBlocking {
        // Given
        val completedTasks = listOf(task3)
        `when`(taskRepository.getCompletedTasks()).thenReturn(flowOf(completedTasks))

        // When
        val result = getTasksUseCase(GetTasksUseCase.Filter.COMPLETED).first()

        // Then
        assertEquals(completedTasks.size, result.size)
        assertEquals(completedTasks.map { it.id }, result.map { it.id })
        verify(taskRepository).getCompletedTasks()
    }

    @Test
    fun `Given there are high priority tasks, When executed with HIGH_PRIORITY filter, Then return only high priority tasks`() = runBlocking {
        // Given
        val highPriorityTasks = allTasks.filter { it.priority == Task.Priority.HIGH }
        `when`(taskRepository.getAllTasks()).thenReturn(flowOf(allTasks))

        // When
        val result = getTasksUseCase(GetTasksUseCase.Filter.HIGH_PRIORITY).first()

        // Then
        assertEquals(highPriorityTasks.size, result.size)
        assertEquals(highPriorityTasks.map { it.id }.sorted(), result.map { it.id }.sorted())
    }
}