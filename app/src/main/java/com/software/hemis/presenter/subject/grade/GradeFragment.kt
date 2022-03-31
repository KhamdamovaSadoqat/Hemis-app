package com.software.hemis.presenter.subject.grade

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentGradeBinding
import com.example.hemis.databinding.TabTaskTypeViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.subject.SubjectViewModel
import com.software.hemis.presenter.subject.adapter.TaskViewPagerAdapter
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.gone
import com.software.hemis.utils.invisible
import com.software.hemis.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.text.FieldPosition

@AndroidEntryPoint
class GradeFragment : BindingFragment<FragmentGradeBinding>(),
    Toolbar.OnMenuItemClickListener {

    private val viewModel: SubjectViewModel by viewModels()
    private val args: GradeFragmentArgs by navArgs()

    var currentTabPosition: Int = 0

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentGradeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener(this)
        setUp()
        setUpViews()
    }

    private fun setUp() {
        setUpToolBar()
    }

    private fun setUpToolBar() {
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK)
        binding.collapsingToolbar.setExpandedTitleColor(Color.BLACK)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpViews() {
        val adapter = TaskViewPagerAdapter(
            loadTitle(),
            childFragmentManager,
            lifecycle,
            args.subjectId
        )
        binding.viewPager.adapter = adapter
        subjectDetails(args.subjectId)
        binding.btnAttendance.setOnClickListener {
            val action =
                GradeFragmentDirections.actionGradeFragmentToAttendanceFragment(args.subjectId)
            findNavController().navigate(action)
        }

        binding.btnResources.setOnClickListener {
            val action =
                GradeFragmentDirections.actionGradeFragmentToResourcesFragment(args.subjectId)
            findNavController().navigate(action)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager, true) { tab, position ->
            val tabView = TabTaskTypeViewBinding.inflate(layoutInflater, binding.tabLayout, false)
            tabView.tvText.text = loadTitle()[position]
            tabView.iv.setImageResource(loadIcons()[position])

            //val a = binding.tabLayout[position].isSelected

            if (position == currentTabPosition) {
                tabView.tvText.visible()
                tabView.iv.visible()
            }

            tab.customView = tabView.root
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tv_text)?.visible()
                tab?.customView?.findViewById<ImageView>(R.id.iv)?.visible()

                if (tab != null) {
                    currentTabPosition = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tv_text)?.gone()
                tab?.customView?.findViewById<ImageView>(R.id.iv)?.visible()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun subjectDetails(subjectId: Int) {
        viewModel.getSubjectDetailsWithSubject(subjectId).observe(this) { subjectDetails ->
            binding.collapsingToolbar.title = subjectDetails.subject.subjectName
            binding.tvSubjectName.text = subjectDetails.subject.subjectName
            binding.tvSubjectScore.text = requireContext().getString(
                R.string.task_count,
                subjectDetails.subjectDetails.submitsCount.toString(),
                subjectDetails.subjectDetails.tasksCount.toString()
            )
            binding.tvBall.text = subjectDetails.subjectDetails.ball.toString()
            binding.tvMaxBall.text = subjectDetails.subjectDetails.maxBall.toString()
            binding.tvBallPercent.text = requireContext().getString(
                R.string.ball_percent,
                subjectDetails.subjectDetails.ball * 100 / subjectDetails.subjectDetails.maxBall,
                "%")
            binding.animatedProgressBar.setProgress(subjectDetails.subjectDetails.submitsCount * 100 / subjectDetails.subjectDetails.tasksCount)
            binding.tvRecourseCount.text = requireContext().getString(
                R.string.recourse_count,
                subjectDetails.subjectDetails.resourcesCount.toString()
            )
            binding.tvAbsentCount.text = requireContext().getString(
                R.string.absent_count,
                subjectDetails.subjectDetails.absentCount.toString()
            )
            binding.tvSubjectDetails.text = requireContext().getString(
                R.string.subject_details,
                subjectDetails.subject.subjectTypeName,
                subjectDetails.subject.totalAcload.toString(),
                subjectDetails.subject.credit.toString()
            )
        }
    }

    private fun loadTitle() = listOf(
        "Topshirishingiz kutulmoqda", //
        "Hafta davomida topshiriqlar",    //
        "Topshiriq topshirildi",      // 12
        "Topshiriq baholandi",        // 13
        "Topshiriq muddati tugadi", //
        "Yangi topshiriq",
        "Hammasi"
    )

    private fun loadIcons() = listOf(
        R.drawable.img_fire,
        R.drawable.img_warning,
        R.drawable.img_done,
        R.drawable.img_marked,
        R.drawable.img_deadline,
        R.drawable.img_new,
        R.drawable.img_all
    )

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.menu_info -> {
                    val action = GradeFragmentDirections.actionGradeFragmentToHelpFragment(
                        Constants.helpTypeDeadline)
                    findNavController().navigate(action)
                }
            }
        }
        return false
    }
}