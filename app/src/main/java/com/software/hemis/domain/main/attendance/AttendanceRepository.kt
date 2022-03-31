package com.software.hemis.domain.main.attendance

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.attendance.AttendanceResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {

    suspend fun attendance(): Flow<BaseResult<List<AttendanceEntity>, WrappedResponse<AttendanceResponse>>>

    suspend fun insertAttendance(attendance: AttendanceEntity)

    suspend fun getAttendanceBySubject(subjectId: Int, semesterId: Int): LiveData<List<AttendanceEntity>>
}