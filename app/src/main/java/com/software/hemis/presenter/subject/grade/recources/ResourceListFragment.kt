package com.software.hemis.presenter.subject.grade.recources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentResourceListBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.subject.SubjectViewModel
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResourceListFragment : BindingFragment<FragmentResourceListBinding>() {

    @Inject
    lateinit var pref: SharedPref
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentResourceListBinding::inflate
    private val viewModel: SubjectViewModel by viewModels()
    lateinit var adapter: ResourcesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp(){
        val position = arguments?.getInt(Constants.BUNDLE_POSITION)
        val subjectId = arguments?.getInt(Constants.BUNDLE_SUBJECT_ID) ?: 0
        adapter = ResourcesAdapter()
        binding.rvAttendance.adapter = adapter
        when(position) {
            Constants.LECTURE_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.LECTURE)
            Constants.LABORATORY_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.LABORATORY)
            Constants.PRACTICAL_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.PRACTICAL)
            Constants.SEMINAR_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.SEMINAR)
            Constants.TRAINING_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.TRAINING)
            Constants.COURSE_WORK_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.COURSE_WORK)
            Constants.INDEPENDENT_WORK_PAGE -> getResources(subjectId, pref.currentSemester?:0, Constants.INDEPENDENT_WORK)
        }

    }

    private fun getResources(subjectId: Int, semesterId: Int, taskId: Int) {
        viewModel.getResource(subjectId, semesterId, taskId).observe(this) {
            adapter.items = it
        }
    }
}