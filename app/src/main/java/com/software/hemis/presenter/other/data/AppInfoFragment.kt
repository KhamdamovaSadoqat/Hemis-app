package com.software.hemis.presenter.other.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentAppInfoBinding
import com.software.hemis.data.comman.base.BindingFragment

class AppInfoFragment : BindingFragment<FragmentAppInfoBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAppInfoBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}