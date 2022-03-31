package com.software.hemis.presenter.subject.grade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentTaskListBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.domain.main.task.TaskWithSubject
import com.software.hemis.presenter.deadline.adapter.TaskRecyclerAdapter
import com.software.hemis.presenter.subject.SubjectViewModel
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.utils.deadlineType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : BindingFragment<FragmentTaskListBinding>() {

    @Inject
    lateinit var pref: SharedPref
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentTaskListBinding::inflate
    private val viewModel: SubjectViewModel by viewModels()
    lateinit var adapter: TaskRecyclerAdapter
    var taskType = 0
    var subjectId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        taskType = args?.getInt(Constants.BUNDLE_POSITION, 0)!!
        subjectId = args.getInt(Constants.BUNDLE_SUBJECT_ID, 0)
        setUpViews()
    }

    private fun setUpViews() {
        adapter = TaskRecyclerAdapter { taskId ->
            val action =
                GradeFragmentDirections.actionGradeFragmentToFragmentUploadTask(taskId.subject.subjectId,
                    taskId.taskEntity.id)
            findNavController().navigate(action)
        }
        binding.rvTask.adapter = adapter
        if (taskType == Constants.WAITING_UPLOAD ||
            taskType == Constants.LESS_THAN_THREE_DAYS ||
            taskType == Constants.NEW_TASK ||
            taskType == Constants.DEADLINE_PASSED
        )
            getTask(subjectId, Constants.GIVEN)
        else if (taskType == Constants.TASK_UPLOADED) getTask(subjectId, Constants.UPLOADED)
        else if (taskType == Constants.TASK_MARKED) getTask(subjectId, Constants.MARKED)
        else if (taskType == Constants.ALL) getAllTask(subjectId)
    }

    private fun getTask(subjectId: Int, taskStatusCode: Int) {
        viewModel.getTask(subjectId, taskStatusCode, pref.currentSemester ?: 0)
            .observe(this) { taskList ->
                if (taskType == Constants.WAITING_UPLOAD ||
                    taskType == Constants.LESS_THAN_THREE_DAYS ||
                    taskType == Constants.NEW_TASK ||
                    taskType == Constants.DEADLINE_PASSED
                ) {
                    val filteredTaskList = arrayListOf<TaskWithSubject>()
                    //need to filter data
                    for (task in taskList.indices) {
                        if (taskType == Constants.LESS_THAN_THREE_DAYS) {
                            when (deadlineType(taskList[task].taskEntity.deadline)) {
                                Constants.LESS_THAN_TWO_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_THREE_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_FOUR_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_FIVE_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_SIX_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_SEVEN_DAYS -> filteredTaskList.add(taskList[task])
                            }
                        } else if (deadlineType(taskList[task].taskEntity.deadline) == taskType)
                            filteredTaskList.add(taskList[task])
                    }
                    if (taskList != null) {
                        adapter.items = filteredTaskList
                    }
                } else adapter.items = taskList
            }
    }

    private fun getAllTask(subjectId: Int) {
        viewModel.getAllTask(subjectId, pref.currentSemester!!).observe(this) { taskList ->
            if (taskList != null) adapter.items = taskList
        }
    }
}