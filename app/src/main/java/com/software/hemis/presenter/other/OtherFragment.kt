package com.software.hemis.presenter.other

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentOtherBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.software.hemis.presenter.other.adapter.DataViewPagerAdapter
import com.software.hemis.presenter.other.data.AppInfoFragment
import com.software.hemis.presenter.other.data.information.InformationFragment
import com.software.hemis.presenter.other.data.profile.ProfileFragment
import com.software.hemis.data.comman.base.BindingFragment

class OtherFragment : BindingFragment<FragmentOtherBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOtherBinding::inflate
    lateinit var adapter:  DataViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        adapter = DataViewPagerAdapter(loadList(), childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager, true) { tab, position ->
            tab.text = loadTabList()[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.appbar.invalidate()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun loadTabList() = listOf("Ma'lumotlar", "Profile", "Ilova haqida")

    private fun loadList(): List<Fragment> =
        listOf(InformationFragment(), ProfileFragment(), AppInfoFragment())
}