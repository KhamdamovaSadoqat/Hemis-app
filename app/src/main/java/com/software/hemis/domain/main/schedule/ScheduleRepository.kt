package com.software.hemis.domain.main.schedule

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.schedule.ScheduleResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun schedule(week: Int):
            Flow<BaseResult<List<ScheduleEntity>, WrappedResponse<ScheduleResponse>>>

    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    suspend fun getSchedule(day: Int): LiveData<List<ScheduleEntity>>

    suspend fun deleteSchedule()
}