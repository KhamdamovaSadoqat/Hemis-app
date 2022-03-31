package com.software.hemis.presenter.subject.grade.attendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentAttendanceBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.subject.SubjectViewModel
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment : BindingFragment<FragmentAttendanceBinding>() {

    @Inject
    lateinit var sharedPref: SharedPref
    private val viewModel: SubjectViewModel by viewModels()
    private val args: AttendanceFragmentArgs by navArgs()
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAttendanceBinding::inflate
    lateinit var adapter: AttendanceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        setUpViews()
        getAttendance()
    }

    private fun getAttendance(){
        Log.d("AttendanceFragment", "------------- getAttendance: subjectId: ${args.subjectId} semesster: ${sharedPref.currentSemester} ")
        viewModel.getAttendance(args.subjectId, sharedPref.currentSemester ?: 0).observe(this){
            Log.d("AttendanceFragment", "------------- getAttendance: $it")
            adapter.items = it
        }
    }

    private fun setUpViews(){
        adapter = AttendanceAdapter()
        binding.rvAttendance.adapter = adapter
    }

    private fun setUpToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}