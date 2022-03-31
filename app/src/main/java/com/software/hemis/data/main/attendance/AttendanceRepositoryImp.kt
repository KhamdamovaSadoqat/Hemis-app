package com.software.hemis.data.main.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.AttendanceDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.main.attendance.AttendanceEntity
import com.software.hemis.domain.main.attendance.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AttendanceRepositoryImp @Inject constructor(
    private val attendanceApi: AttendanceApi,
    private val attendanceDao: AttendanceDao,
) : AttendanceRepository {
    override suspend fun attendance(): Flow<BaseResult<List<AttendanceEntity>, WrappedResponse<AttendanceResponse>>> {
        return flow {
            val response = attendanceApi.resource()
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    val listAttendance = arrayListOf<AttendanceEntity>()
                    Log.d("AttendanceRepositoryImp", "------------- attendance: ${body.data?.size}")
                    Log.d("AttendanceRepositoryImp", "------------- attendance: ${body.data}")
                    body.data!!.forEach { data ->
                        val attendanceEntity = AttendanceEntity(
                            data.subject?.id ?: 0,
                            data.semester?.code?.toInt() ?: 0,
                            data.subject?.name ?: "",
                            data.trainingType?.code?.toInt() ?: 0,
                            data.trainingType?.name ?: "",
                            data.lessonPair?.code?.toInt() ?: 0,
                            data.lessonPair?.name?: "",
                            data.lessonPair?.startTime?: "",
                            data.lessonPair?.endTime?: "",
                            data.absentOff ?: 0,
                            data.absentOn ?: 0,
                            data.lessonDate ?: 0,
                            "${data.lessonDate}${data.subject?.id}${data.lessonPair?.code}" ,

                        )
                        listAttendance.add(attendanceEntity)
                    }
                    emit(BaseResult.Success(listAttendance))
                }
            }else {
                val type = object : TypeToken<WrappedResponse<AttendanceResponse>>() {}.type
                val err: WrappedResponse<AttendanceResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun insertAttendance(attendance: AttendanceEntity) {
        attendanceDao.insertAttendance(attendance)
    }

    override suspend fun getAttendanceBySubject(
        subjectId: Int,
        semesterId: Int,
    ): LiveData<List<AttendanceEntity>> {
        return attendanceDao.getAttendanceBySubject(subjectId, semesterId)
    }
}