package com.software.hemis.presenter.other.data.profile.data

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentAttendanceAllBinding
import com.software.hemis.data.comman.base.BindingFragment

class AttendanceAllFragment : BindingFragment<FragmentAttendanceAllBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAttendanceAllBinding::inflate

}