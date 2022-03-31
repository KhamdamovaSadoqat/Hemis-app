package com.software.hemis.presenter.deadline

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.DialogFilterBinding
import com.example.hemis.databinding.FragmentDeadlineBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.domain.main.task.TaskWithSubject
import com.software.hemis.presenter.deadline.adapter.TaskRecyclerAdapter
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.utils.deadlineType
import com.software.hemis.utils.invisible
import com.software.hemis.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeadlineFragment : BindingFragment<FragmentDeadlineBinding>(),
    View.OnClickListener,
    Toolbar.OnMenuItemClickListener {

    @Inject
    lateinit var pref: SharedPref
    private val viewModel: DeadlineViewModel by viewModels()
    private lateinit var filterDialogBinding: DialogFilterBinding
    private var filterDialog: BottomSheetDialog? = null
    private lateinit var adapter: TaskRecyclerAdapter
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentDeadlineBinding::inflate

    lateinit var lastClickedCheckBox: RadioButton

    // the default status of showing tasks which means
    // 0 == less than a day,
    // 1 == within a week
    // 5 == new task
    // 6 == all tasks
    private var defaultStatus = Constants.WAITING_UPLOAD

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterDialogBinding = DialogFilterBinding.inflate(LayoutInflater.from(requireContext()))
        binding.toolbar.setOnMenuItemClickListener(this)
        setUpViews()
    }

    override fun onResume() {
        super.onResume()
        defaultStatus = Constants.WAITING_UPLOAD
        getAllDeadlineTask(Constants.GIVEN, Constants.WAITING_UPLOAD)
    }

    private fun setUpViews() {
        adapter = TaskRecyclerAdapter {
            val action =
                DeadlineFragmentDirections.actionNavigationDeadlineToFragmentUploadTask(it.subject.subjectId,
                    it.taskEntity.id)
            findNavController().navigate(action)
        }
        binding.rvTask.adapter = adapter
        binding.collapsingToolbar.title = "Topshiriqlar ${pref.currentSemesterName}"
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK)
        binding.collapsingToolbar.setExpandedTitleColor(Color.BLACK)

        lastClickedCheckBox = filterDialogBinding.chbPending
        filterDialogBinding.chbPending.isChecked = true

        binding.cardFilter.setOnClickListener {
            (filterDialogBinding.root.parent as? ViewGroup)?.removeView(filterDialogBinding.root)
            showFilterDialog()
        }

        filterDialogBinding.chbAll.setOnClickListener(this)
        filterDialogBinding.chbPending.setOnClickListener(this)
        filterDialogBinding.chbThreeDays.setOnClickListener(this)
        filterDialogBinding.chbSubmitted.setOnClickListener(this)
        filterDialogBinding.chbGraded.setOnClickListener(this)
        filterDialogBinding.chbNew.setOnClickListener(this)
        filterDialogBinding.chbDeadline.setOnClickListener(this)
        filterDialogBinding.btnSubmit.setOnClickListener(this)
        filterDialogBinding.btnClear.setOnClickListener(this)
    }

    private fun showFilterDialog() {
        filterDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        filterDialog!!.setContentView(filterDialogBinding.root)
        filterDialog!!.show()
    }

    private fun dismissFilterDialog() {
        if (filterDialog!!.isShowing) {
            filterDialog!!.dismiss()
        }
        filterDialog!!.dismiss()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                filterDialogBinding.chbAll.id -> {
                    getAllTask()
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbAll
                }
                filterDialogBinding.chbPending.id -> {
                    getAllDeadlineTask(Constants.GIVEN, Constants.WAITING_UPLOAD)
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbPending
                }
                filterDialogBinding.chbThreeDays.id -> {
                    getAllDeadlineTask(Constants.GIVEN, Constants.LESS_THAN_THREE_DAYS)
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbThreeDays
                }
                filterDialogBinding.chbSubmitted.id -> {
                    getAllUploadedTask(Constants.UPLOADED)
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbSubmitted
                }
                filterDialogBinding.chbGraded.id -> {
                    getAllUploadedTask(Constants.MARKED)
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbGraded
                }
                filterDialogBinding.chbNew.id -> {
                    getAllDeadlineTask(Constants.GIVEN, Constants.NEW_TASK)
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbNew
                }
                filterDialogBinding.chbDeadline.id -> {
                    getAllDeadlineTask(Constants.GIVEN, Constants.DEADLINE_PASSED)
                    lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbDeadline
                }
                filterDialogBinding.btnSubmit.id -> {
                    dismissFilterDialog()
                }
                filterDialogBinding.btnClear.id -> {
                    getAllDeadlineTask(Constants.GIVEN, Constants.LESS_THAN_THREE_DAYS)
                    if (lastClickedCheckBox != filterDialogBinding.chbThreeDays)
                        lastClickedCheckBox.isChecked = false
                    lastClickedCheckBox = filterDialogBinding.chbThreeDays
                }
            }
        }
    }

    private fun getAllTask() {
        viewModel.getAllTask(pref.currentSemester ?: 0).observe(this) { taskList ->
            binding.rvTask.visible()
            binding.spinKit.invisible()
            adapter.items = taskList

        }
    }

    private fun getAllDeadlineTask(taskStatusCode: Int, deadlineType: Int) {
        viewModel.getAllDeadlineTask(taskStatusCode, pref.currentSemester ?: 0)
            .observe(this) { taskList ->
                    binding.rvTask.visible()
                    binding.spinKit.invisible()
                    val filteredTaskList = arrayListOf<TaskWithSubject>()
                    //need to filter data and remove passed deadlines
                    for (task in taskList.indices) {
                        if (deadlineType == Constants.LESS_THAN_THREE_DAYS) {
                            when (deadlineType(taskList[task].taskEntity.deadline)) {
                                Constants.LESS_THAN_TWO_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_THREE_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_FOUR_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_FIVE_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_SIX_DAYS -> filteredTaskList.add(taskList[task])
                                Constants.LESS_THAN_SEVEN_DAYS -> filteredTaskList.add(taskList[task])
                            }
                        } else if (deadlineType(taskList[task].taskEntity.deadline) == deadlineType)
                            filteredTaskList.add(taskList[task])
                    }
                if (filteredTaskList.isNotEmpty()) {
                    adapter.items = filteredTaskList
                } else {
                    defaultStatus++
                    when (defaultStatus) {
                        Constants.LESS_THAN_THREE_DAYS -> {
                            getAllDeadlineTask(taskStatusCode, Constants.LESS_THAN_THREE_DAYS)
                            defaultStatus += 4
                            lastClickedCheckBox = filterDialogBinding.chbThreeDays
                            filterDialogBinding.chbThreeDays.isChecked = true
                        }
                        Constants.NEW_TASK -> {
                            getAllDeadlineTask(taskStatusCode, defaultStatus)
                            defaultStatus++
                            lastClickedCheckBox = filterDialogBinding.chbNew
                            filterDialogBinding.chbNew.isChecked = true
                        }
                        Constants.ALL -> {
                            getAllDeadlineTask(taskStatusCode, defaultStatus)
                            lastClickedCheckBox = filterDialogBinding.chbAll
                            filterDialogBinding.chbAll.isChecked = true
                        }
                    }
                }

            }
    }

    private fun getAllUploadedTask(taskStatusCode: Int) {
        viewModel.getAllDeadlineTask(taskStatusCode, pref.currentSemester ?: 0)
            .observe(this) { taskList ->
                binding.rvTask.visible()
                binding.spinKit.invisible()
                adapter.items = taskList
            }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.menu_info -> {
                    val action = DeadlineFragmentDirections.actionNavigationDeadlineToHelpFragment(
                        Constants.helpTypeDeadline)
                    findNavController().navigate(action)
                }
            }
        }
        return false
    }
}