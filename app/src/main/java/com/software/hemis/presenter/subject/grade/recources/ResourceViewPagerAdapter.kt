package com.software.hemis.presenter.subject.grade.recources

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.software.hemis.utils.data.Constants

class ResourceViewPagerAdapter (
    private val list: List<String>,
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val subjectId: Int
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ResourceListFragment()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_POSITION, position)
        args.putInt(Constants.BUNDLE_SUBJECT_ID, subjectId)
        fragment.arguments = args
        return fragment
    }
}