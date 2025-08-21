package com.example.todo_android.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo_android.R
import com.example.todo_android.presentation.ui.tasks.list.TaskListFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 메인 액티비티
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 처음 실행 시 TaskListFragment 표시
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TaskListFragment())
                .commit()
        }
    }

    /**
     * Fragment 교체 헬퍼 메서드
     */
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
