package com.software.hemis.domain.main.semester

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.semester.SemesterResponse
import com.software.hemis.domain.base.BaseResultList
import kotlinx.coroutines.flow.Flow

interface SemesterRepository {
    suspend fun semester(): Flow<BaseResultList<List<SemesterEntity>, List<WeekEntity>, WrappedResponse<SemesterResponse>>>

    suspend fun insertSemester(semesterEntity: SemesterEntity)

    suspend fun insertWeek(weekEntity: WeekEntity)

    suspend fun getSemester(): LiveData<List<SemesterEntity>>

    suspend fun getCurrentSemester(currentSemester: Int): LiveData<SemesterEntity>
}