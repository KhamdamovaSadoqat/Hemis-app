package com.software.hemis.presenter.auth.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentIntroductionBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.utils.getNavOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroductionFragment : BindingFragment<FragmentIntroductionBinding>() {

    @Inject
    lateinit var sharedPref: SharedPref

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentIntroductionBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // viewPager
       val viewPagerAdapter = ViewPagerAdapter(Constants.getIntro(), requireContext())
        binding.vpPictures.adapter = viewPagerAdapter
        //indicator
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.setViewPager(binding.vpPictures)

        sharedPref.isFirstOpen = true

        binding.btnHarakatniBoshlash.setOnClickListener {
            findNavController().navigate(R.id.universityFragment, null, getNavOptions().build())
        }
    }
}