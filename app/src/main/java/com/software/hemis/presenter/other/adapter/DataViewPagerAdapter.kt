package com.software.hemis.presenter.other.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class DataViewPagerAdapter(
    private val l: List<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return l.size
    }

    override fun createFragment(position: Int): Fragment {
        return l[position]
    }
}
