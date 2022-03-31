package com.software.hemis.data.main.schedule

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.ScheduleDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.main.schedule.ScheduleEntity
import com.software.hemis.domain.main.schedule.ScheduleRepository
import com.software.hemis.utils.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScheduleRepositoryImp @Inject constructor(
    private val scheduleApi: ScheduleApi,
    private val scheduleDao: ScheduleDao,
) : ScheduleRepository {
    override suspend fun schedule(week: Int): Flow<BaseResult<List<ScheduleEntity>, WrappedResponse<ScheduleResponse>>> {
        return flow {
            val response = scheduleApi.schedule("${Constants.universityUrl}education/schedule?week=$week"
            )
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    val listScheduleEntity = arrayListOf<ScheduleEntity>()
                    for (schedule in body.data?.indices!!) {
                        val scheduleEntity = ScheduleEntity(
                            body.data!![schedule].subject?.name ?: "",
                            body.data!![schedule].subject?.id ?: -1,
                            body.data!![schedule].week ?: -1,
                            body.data!![schedule].auditorium?.name ?: "",
                            body.data!![schedule].auditorium?.code ?: -1,
                            body.data!![schedule].trainingType?.name ?: "",
                            body.data!![schedule].trainingType?.code ?: "",
                            body.data!![schedule].lessonDate ?: -1,
                            body.data!![schedule].lessonPair?.code ?: "",
                            body.data!![schedule].lessonPair?.name ?: "",
                            body.data!![schedule].lessonPair?.startTime ?: "",
                            body.data!![schedule].employee?.name ?: "",
                            body.data!![schedule].employee?.id ?: -1,
                            body.data!![schedule].additional ?: "",
                            "${body.data!![schedule].lessonDate}${body.data!![schedule].lessonPair?.startTime}"
                        )
                        listScheduleEntity.add(scheduleEntity)
                    }
                    emit(BaseResult.Success(listScheduleEntity))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<ScheduleResponse>>() {}.type
                val err: WrappedResponse<ScheduleResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun insertSchedule(scheduleEntity: ScheduleEntity) {
        scheduleDao.insertSchedule(scheduleEntity)
    }

    override suspend fun getSchedule(day: Int): LiveData<List<ScheduleEntity>> {
        return scheduleDao.getSchedule(day)
    }

    override suspend fun deleteSchedule() {
        scheduleDao.deleteSchedule()
    }
}