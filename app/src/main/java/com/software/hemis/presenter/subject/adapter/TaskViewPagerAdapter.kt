package com.software.hemis.presenter.subject.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.software.hemis.presenter.subject.grade.TaskListFragment
import com.software.hemis.utils.data.Constants

class TaskViewPagerAdapter(
    private val list: List<String>,
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val subjectId: Int
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return list.size
    }

//   0 -> "Topshirishingiz kutulmoqda",
//   1 -> "3 kundan kam vaqt qoldi",
//   2 -> "Yangii topshiriq",
//   3 -> "Topshiriq topshirildi",
//   4 -> "Topshiriq baholandi",
//   5 -> "Topshiriq muddati tugadi",
//   6 -> "Yangi topshiriq",
//   7 -> "Hammasi"

    override fun createFragment(position: Int): Fragment {
        val fragment = TaskListFragment()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_POSITION, position)
        args.putInt(Constants.BUNDLE_SUBJECT_ID, subjectId)
        fragment.arguments = args
        return fragment
    }
}