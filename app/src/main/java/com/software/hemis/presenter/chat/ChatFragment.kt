package com.software.hemis.presenter.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentChatBinding
import com.software.hemis.data.comman.base.BindingFragment

class ChatFragment: BindingFragment<FragmentChatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChatBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}