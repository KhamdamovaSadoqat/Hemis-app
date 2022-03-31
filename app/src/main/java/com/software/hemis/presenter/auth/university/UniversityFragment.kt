package com.software.hemis.presenter.auth.university

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentUniversityBinding
import com.google.android.material.textfield.TextInputLayout
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.domain.auth.university.UniversityEntity
import com.software.hemis.presenter.auth.LoginViewModel
import com.software.hemis.presenter.auth.UniversityState
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.utils.gone
import com.software.hemis.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class UniversityFragment : BindingFragment<FragmentUniversityBinding>(), TextWatcher {

    @Inject
    lateinit var pref: SharedPref
    private val viewModel: LoginViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityBinding::inflate
    lateinit var adapter: UniversityAdapter
    lateinit var list: List<UniversityEntity>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setUp()
        university()
        observe()
    }

    private fun setUp() {
        var chosenUni = UniversityEntity(0, "", "")
//        val list = resources.getStringArray(R.array.university)
        adapter = UniversityAdapter {
            chosenUni = it
            binding.etUni.setText(it.name)
        }
        binding.rvUniversity.adapter = adapter
        binding.btnNext.setOnClickListener {
            if (chosenUni.code != 0) {
                binding.tvLayoutFrom.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), R.color.grey_lite)
                pref.universityUrl = chosenUni.apiUrl
                Constants.universityUrl = chosenUni.apiUrl
                findNavController().navigate(R.id.loginFragment)
            } else {
                binding.tvLayoutFrom.requestFocus()
                binding.tvLayoutFrom.endIconMode = TextInputLayout.END_ICON_NONE
                binding.tvLayoutFrom.error = " "
            }
        }
    }

    fun university() {
        viewModel.university()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mUniversityState.collectLatest {
                handleStateChange(it)
            }
        }
    }

    private fun handleStateChange(state: UniversityState) {
        when (state) {
            is UniversityState.Init -> Unit
            is UniversityState.ErrorUniversity -> {

            }
            is UniversityState.ShowToast -> {}
            is UniversityState.SuccessUniversity -> {
                adapter.items = state.university
                list = state.university
                binding.etUni.addTextChangedListener(this)
            }
            is UniversityState.IsLoading -> {
                if (state.isLoading) {
                    binding.spinKit.visible()
                } else {
                    binding.spinKit.gone()
                }
            }
        }
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<UniversityEntity> = ArrayList()
        for (item in list) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isNotEmpty()) {
            adapter.items = filteredList
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etUni.setText("")
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        filter(p0.toString())
    }

}