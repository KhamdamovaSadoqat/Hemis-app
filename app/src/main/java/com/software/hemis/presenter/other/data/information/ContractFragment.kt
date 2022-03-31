package com.software.hemis.presenter.other.data.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentContractBinding
import com.software.hemis.data.comman.base.BindingFragment

class ContractFragment : BindingFragment<FragmentContractBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentContractBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView(){
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}