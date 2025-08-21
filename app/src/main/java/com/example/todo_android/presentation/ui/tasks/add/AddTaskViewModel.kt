package com.example.todo_android.presentation.ui.tasks.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_android.domain.model.Task
import com.example.todo_android.domain.usecase.AddTaskUseCase
import com.example.todo_android.presentation.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * 태스크 추가 화면의 ViewModel
 */
@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _saveResult = MutableLiveData<Event<Boolean>>()
    val saveResult: LiveData<Event<Boolean>> = _saveResult

    /**
     * 새 태스크 저장
     */
    fun saveTask(
        title: String,
        description: String,
        dueDate: Date?,
        priority: Task.Priority
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                addTaskUseCase(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = priority
                )
                _saveResult.value = Event(true)
            } catch (e: Exception) {
                _saveResult.value = Event(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
