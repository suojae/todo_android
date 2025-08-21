package com.example.todo_android.presentation.ui.tasks.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.example.todo_android.R
import com.example.todo_android.domain.usecase.GetTasksUseCase
import com.example.todo_android.presentation.ui.MainActivity
import com.example.todo_android.presentation.ui.tasks.add.AddTaskFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 태스크 목록을 표시하는 Fragment
 */
@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var tasksAdapter: TasksAdapter
    
    // Views
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvCurrentFilter: TextView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        
        initViews(view)
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }
    
    private fun initViews(view: View) {
        rvTasks = view.findViewById(R.id.rvTasks)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)
        progressBar = view.findViewById(R.id.progressBar)
        tvCurrentFilter = view.findViewById(R.id.tvCurrentFilter)
        fabAddTask = view.findViewById(R.id.fabAddTask)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            R.id.menu_refresh -> {
                viewModel.loadTasks(viewModel.currentFilter.value!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        tasksAdapter = TasksAdapter(
            onTaskClick = { task -> viewModel.navigateToTaskDetail(task.id) },
            onTaskCheckedChange = { task -> viewModel.toggleTaskCompleted(task) },
            onTaskDelete = { task -> viewModel.deleteTask(task) }
        )

        rvTasks.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        // 태스크 목록 관찰
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasksAdapter.submitList(tasks)
            tvEmptyState.isVisible = tasks.isEmpty()
        }

        // 로딩 상태 관찰
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.isVisible = isLoading
        }

        // 현재 필터 관찰
        viewModel.currentFilter.observe(viewLifecycleOwner) { filter ->
            tvCurrentFilter.text = getFilterText(filter)
        }

        // 스낵바 메시지 관찰
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
            }
        }

        // 상세 화면 네비게이션 관찰 (임시로 비활성화)
        // viewModel.navigationToDetail.observe(viewLifecycleOwner) { event ->
        //     event.getContentIfNotHandled()?.let { taskId ->
        //         // TODO: Implement task detail screen
        //     }
        // }
    }

    private fun setupListeners() {
        // FAB 클릭 시 새 태스크 추가 화면으로 이동
        fabAddTask.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(AddTaskFragment())
        }

        // 스와이프 리프레시 설정
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadTasks(viewModel.currentFilter.value!!)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener { item ->
                val filter = when (item.itemId) {
                    R.id.filter_all -> GetTasksUseCase.Filter.ALL
                    R.id.filter_active -> GetTasksUseCase.Filter.ACTIVE
                    R.id.filter_completed -> GetTasksUseCase.Filter.COMPLETED
                    R.id.filter_high_priority -> GetTasksUseCase.Filter.HIGH_PRIORITY
                    R.id.filter_medium_priority -> GetTasksUseCase.Filter.MEDIUM_PRIORITY
                    R.id.filter_low_priority -> GetTasksUseCase.Filter.LOW_PRIORITY
                    R.id.filter_with_deadline -> GetTasksUseCase.Filter.WITH_DEADLINE
                    R.id.filter_overdue -> GetTasksUseCase.Filter.OVERDUE
                    else -> GetTasksUseCase.Filter.ALL
                }
                viewModel.setFilter(filter)
                true
            }
            show()
        }
    }

    private fun getFilterText(filter: GetTasksUseCase.Filter): String {
        return when (filter) {
            GetTasksUseCase.Filter.ALL -> "All Tasks"
            GetTasksUseCase.Filter.ACTIVE -> "Active Tasks"
            GetTasksUseCase.Filter.COMPLETED -> "Completed Tasks"
            GetTasksUseCase.Filter.HIGH_PRIORITY -> "High Priority Tasks"
            GetTasksUseCase.Filter.MEDIUM_PRIORITY -> "Medium Priority Tasks"
            GetTasksUseCase.Filter.LOW_PRIORITY -> "Low Priority Tasks"
            GetTasksUseCase.Filter.WITH_DEADLINE -> "Tasks with Deadline"
            GetTasksUseCase.Filter.OVERDUE -> "Overdue Tasks"
        }
    }
}
