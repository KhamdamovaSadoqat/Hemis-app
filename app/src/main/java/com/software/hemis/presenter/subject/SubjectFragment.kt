package com.software.hemis.presenter.subject

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentSubjectBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.subject.adapter.SubjectAdapter
import com.software.hemis.utils.*
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SubjectFragment : BindingFragment<FragmentSubjectBinding>(), Toolbar.OnMenuItemClickListener {

    @Inject
    lateinit var sharedPref: SharedPref
    private val viewModel: SubjectViewModel by viewModels()
    private lateinit var adapter: SubjectAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSubjectBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener(this)
        setUp()
        subjects()
    }


    private fun setUp() {
        adapter = SubjectAdapter {
            val action = SubjectFragmentDirections.actionNavigationSubjectToGradeFragment(it)
            findNavController().navigate(action)
        }
        binding.rvSubjects.adapter = adapter
        binding.toolbar.title = "Fanlar ${sharedPref.currentSemesterName}"

    }

    private fun subjects() {
        sharedPref.currentSemester?.let { semesterId ->
            viewModel.getSubjectWithSubjectDetails(semesterId).observe(this) { subjectDetails ->
                binding.spinKit.gone()
                adapter.items = subjectDetails
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.menu_info ->  {
                    val action = SubjectFragmentDirections.actionNavigationSubjectToHelpFragment(Constants.helpTypeSubject)
                        findNavController().navigate(action)
                }
            }
        }
        return false
    }
}