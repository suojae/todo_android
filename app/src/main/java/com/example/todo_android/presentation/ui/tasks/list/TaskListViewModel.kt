package com.example.todo_android.presentation.ui.tasks.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_android.domain.model.Task
import com.example.todo_android.domain.usecase.DeleteTaskUseCase
import com.example.todo_android.domain.usecase.GetTasksUseCase
import com.example.todo_android.domain.usecase.UpdateTaskUseCase
import com.example.todo_android.presentation.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 태스크 목록 화면의 ViewModel
 */
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    // UI 상태
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentFilter = MutableLiveData(GetTasksUseCase.Filter.ALL)
    val currentFilter: LiveData<GetTasksUseCase.Filter> = _currentFilter

    // 일회성 이벤트
    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> = _snackbarMessage

    private val _navigationToDetail = MutableLiveData<Event<Long>>()
    val navigationToDetail: LiveData<Event<Long>> = _navigationToDetail

    init {
        loadTasks(_currentFilter.value!!)
    }

    /**
     * 태스크 목록을 로드
     */
    fun loadTasks(filter: GetTasksUseCase.Filter) {
        getTasksUseCase(filter)
            .onStart { _isLoading.value = true }
            .onEach {
                _tasks.value = it
                _isLoading.value = false
                _currentFilter.value = filter
            }
            .catch { e ->
                _isLoading.value = false
                showSnackbarMessage("Error loading tasks: ${e.localizedMessage}")
            }
            .launchIn(viewModelScope)
    }

    /**
     * 필터 변경
     */
    fun setFilter(filter: GetTasksUseCase.Filter) {
        if (filter != _currentFilter.value) {
            loadTasks(filter)
        }
    }

    /**
     * 태스크 완료 상태 토글
     */
    fun toggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            try {
                val updatedTask = if (task.isCompleted) {
                    task.markAsActive()
                } else {
                    task.markAsCompleted()
                }

                updateTaskUseCase(updatedTask)

                val message = if (updatedTask.isCompleted) "Task marked as completed" else "Task marked as active"
                showSnackbarMessage(message)
            } catch (e: Exception) {
                showSnackbarMessage("Error updating task: ${e.localizedMessage}")
            }
        }
    }

    /**
     * 태스크 삭제
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(task)
                showSnackbarMessage("Task deleted successfully")
            } catch (e: Exception) {
                showSnackbarMessage("Error deleting task: ${e.localizedMessage}")
            }
        }
    }

    /**
     * 태스크 상세 화면으로 이동
     */
    fun navigateToTaskDetail(taskId: Long) {
        _navigationToDetail.value = Event(taskId)
    }

    /**
     * 스낵바 메시지 표시
     */
    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = Event(message)
    }
}