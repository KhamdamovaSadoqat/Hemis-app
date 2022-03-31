package com.software.hemis.presenter.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentHelpBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.utils.data.Constants

class HelpFragment : BindingFragment<FragmentHelpBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHelpBinding::inflate
    private val args: HelpFragmentArgs by navArgs()
    private lateinit var adapter: HelpAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp(){
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivHelp.setImageDrawable(Constants.getHelpPic(args.helpType, requireContext()))
        adapter = HelpAdapter()
        binding.rvHelp.adapter = adapter
        adapter.items = Constants.getHelp(args.helpType)
    }

}