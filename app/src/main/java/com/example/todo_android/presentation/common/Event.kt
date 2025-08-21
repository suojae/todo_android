package com.example.todo_android.presentation.common

/**
 * 일회성 이벤트를 처리하기 위한 래퍼 클래스
 * UI 이벤트가 configuration change 등으로 인해 중복 실행되는 것을 방지합니다.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * 콘텐츠를 반환하고, 이미 처리되었음을 표시합니다.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
