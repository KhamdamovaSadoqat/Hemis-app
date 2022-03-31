package com.software.hemis.presenter.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentScheduleBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.schedule.adapter.ScheduleAdapter
import com.software.hemis.presenter.schedule.adapter.ScheduleDaysAdapter
import com.software.hemis.utils.*
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : BindingFragment<FragmentScheduleBinding>(),
    Toolbar.OnMenuItemClickListener {

    @Inject
    lateinit var sharedPref: SharedPref
    lateinit var adapter: ScheduleAdapter
    lateinit var daysAdapter: ScheduleDaysAdapter
    private lateinit var weekStartEnd: WeekTime
    private val viewModel: ScheduleViewModel by viewModels()
    private var currentWeek: Int = 0
    private var weekDays = listOf<IsToday>()

    // is calendar top days is set or not
    private var setTopCalendarDays = false

    // is calendar week have today day or not
    private var isHave = false

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentScheduleBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentWeek = sharedPref.currentWeek!!
        binding.toolbar.setOnMenuItemClickListener(this)
    }

    private fun setUp() {
        adapter = ScheduleAdapter { }
        daysAdapter = ScheduleDaysAdapter { position ->
            val newWeek = arrayListOf<IsToday>()
            for (day in weekDays.indices) {
                if (position == day) {
                    newWeek.add(IsToday(weekDays[day].day, true, weekDays[day].todayInSeconds))
                } else newWeek.add(IsToday(weekDays[day].day, false, weekDays[day].todayInSeconds))
            }
            getSchedule(newWeek[position].todayInSeconds)
            daysAdapter.items = newWeek
            weekDays = newWeek
        }
        binding.rvTable.adapter = adapter
        binding.rvDaysTable.adapter = daysAdapter
        binding.btnBack.setOnClickListener {
            if (currentWeek - 1 >= sharedPref.weekMinId!!) {

                //need to be set again
                setTopCalendarDays = false
                getScheduleMonday(currentWeek - 1)
                currentWeek -= 1
            } else {
                binding.root.context.showToast("Jadvalning boshiga yetdingiz.")
            }
        }
        binding.btnNext.setOnClickListener {
            if (currentWeek + 1 <= sharedPref.weekMaxId!!) {
                //need to be set again
                setTopCalendarDays = false
                getScheduleMonday(currentWeek + 1)
                currentWeek += 1
            } else {
                binding.root.context.showToast("Jadvalning oxiriga yetdingiz.")
            }
        }
    }

    private fun setUpCalendarTopDays(weekTime: WeekTime) {
        val month = requireContext().getString(
            R.string.month_month,
            weekTime.week[0].day,
            requireContext().getString(Constants.getMonth()[weekTime.star.toInt() - 1]),
            weekTime.week[6].day,
            requireContext().getString(Constants.getMonth()[weekTime.end.toInt() - 1])
        )
        binding.tvWeek.text = month
    }

    private fun getSchedule(day: Int) {
        viewModel.getSchedule(day).observe(this) { scheduleEntity ->
            if (scheduleEntity.isNotEmpty()) {
                Log.d("ScheduleFragment", "------------- getSchedule: not empty")
                adapter.items = scheduleEntity
                binding.tvNoSchedule.gone()
            } else {
                Log.d("ScheduleFragment", "------------- getSchedule: empty")
                adapter.items = emptyList()
                binding.tvNoSchedule.visible()
            }
        }
        Log.d("ScheduleFragment",
            "------------- getSchedule: currentweek: ${sharedPref.currentWeek}")
        sharedPref.currentWeek?.let {
            viewModel.getWeek(it).observe(this) { week ->
                if (week == null) {
                    binding.tvNoScheduleAtAll.visible()

                    binding.tvNoSchedule.gone()
                    binding.rvDaysTable.gone()
                    binding.btnNext.gone()
                    binding.btnBack.gone()
                    binding.btnWeek.gone()
                    binding.llWeekName.gone()
                    binding.llWeekNumber.gone()
                    binding.rvTable.gone()
                    binding.div.gone()
                } else {
                    //generating all thing including days times and current days
                    weekStartEnd = weekStartEnd(week.startTime, week.endTime)
                    if (!setTopCalendarDays) {
                        weekDays = weekStartEnd.week
                        daysAdapter.items = weekDays
                        setUpCalendarTopDays(weekStartEnd)
                        setTopCalendarDays = true
                    }

                }
            }
        }
    }

    private fun getScheduleMonday(weekId: Int) {
        viewModel.getWeek(weekId).observe(this) { weekEntity ->
            weekStartEnd = weekStartEnd(weekEntity.startTime, weekEntity.endTime)
            if (!setTopCalendarDays) {
                //specially checking if today is true or not
                // because in some week these is no today so monday should be default set
                isHave = false
                for (week in weekStartEnd.week.indices) {
                    if (weekStartEnd.week[week].today) isHave = true
                }
                if (!isHave) {
                    weekStartEnd.week[0].today = true
                }
                weekDays = weekStartEnd.week
                daysAdapter.items = weekDays
                setUpCalendarTopDays(weekStartEnd)
                setTopCalendarDays = true
            }

            // chaking if today is have because if it is have we should demonstrate this day
            // if not demonstrate monday -> week start
            var day = getTodayInSeconds()
            if (!isHave) day = weekEntity.startTime
            viewModel.getSchedule(day).observe(this) { scheduleEntity ->
                if (scheduleEntity.isNotEmpty()) {
                    adapter.items = scheduleEntity
                    binding.tvNoSchedule.gone()
                    binding.tvNoScheduleAtAll.gone()

                    binding.rvDaysTable.visible()
                    binding.btnNext.visible()
                    binding.btnBack.visible()
                    binding.btnWeek.visible()
                    binding.div.visible()
                    binding.llWeekName.visible()
                    binding.llWeekNumber.visible()
                    binding.rvTable.visible()
                } else {
                    adapter.items = emptyList()
                    binding.tvNoSchedule.visible()
                    binding.tvNoScheduleAtAll.gone()

                    binding.rvDaysTable.visible()
                    binding.btnNext.visible()
                    binding.btnBack.visible()
                    binding.btnWeek.visible()
                    binding.div.visible()
                    binding.llWeekName.visible()
                    binding.llWeekNumber.visible()
                    binding.rvTable.visible()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ScheduleFragment", "------------- onResume: ")
        setUp()
        getSchedule(getTodayInSeconds())
        setTopCalendarDays = false
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.menu_info -> {
                    val action =
                        ScheduleFragmentDirections.actionNavigationTableToHelpFragment(Constants.helpTypeSchedule)
                    findNavController().navigate(action)
                }
            }
        }
        return false
    }
}