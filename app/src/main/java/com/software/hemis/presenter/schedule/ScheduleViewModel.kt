package com.software.hemis.presenter.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.schedule.ScheduleResponse
import com.software.hemis.database.room.ScheduleDao
import com.software.hemis.database.room.WeekDao
import com.software.hemis.domain.main.schedule.ScheduleEntity
import com.software.hemis.domain.main.semester.WeekEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val weekDao: WeekDao
) : ViewModel() {

    fun getSchedule(day: Int): LiveData<List<ScheduleEntity>> {
        return scheduleDao.getSchedule(day)
    }

    fun getWeek(weekId: Int): LiveData<WeekEntity>{
        return  weekDao.getWeek(weekId)
    }
}

sealed class ScheduleState {
    object Init : ScheduleState()
    data class IsLoading(val isLoading: Boolean) : ScheduleState()
    data class ShowToast(val message: String) : ScheduleState()
    data class SuccessSchedule(val scheduleEntity: List<ScheduleEntity>) : ScheduleState()
    data class ErrorSchedule(val rawResponse: WrappedResponse<ScheduleResponse>) : ScheduleState()
}