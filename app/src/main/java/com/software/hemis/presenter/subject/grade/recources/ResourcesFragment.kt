package com.software.hemis.presenter.subject.grade.recources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.hemis.databinding.FragmentResourcesBinding
import com.example.hemis.databinding.TabResourceTypeViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.subject.SubjectViewModel
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResourcesFragment : BindingFragment<FragmentResourcesBinding>() {

    @Inject
    lateinit var sharedPref: SharedPref
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentResourcesBinding::inflate

    private val args: ResourcesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        setUpViews()
        setUpTab()
    }

    private fun setUpTab() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager, true) { tab, position ->
            val tabView =
                TabResourceTypeViewBinding.inflate(layoutInflater, binding.tabLayout, false)
            tabView.tvText.text = Constants.loadResourceTitle(requireContext())[position]
            tab.customView = tabView.root
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setUpViews() {
        val viewPagerAdapter = ResourceViewPagerAdapter(
            Constants.loadResourceTitle(requireContext()),
            childFragmentManager,
            lifecycle,
            args.subjectId
        )
        binding.viewPager.adapter = viewPagerAdapter
    }

    private fun setUpToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }




}