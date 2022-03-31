package com.software.hemis.domain.main.subject

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.subjects.SubjectResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    suspend fun subjects():
            Flow<BaseResult<List<SubjectEntity>, WrappedResponse<SubjectResponse>>>

    suspend fun insertSubject(subjectEntity: SubjectEntity)

    suspend fun getSubjectList(semesterCode: Int): LiveData<List<SubjectEntity>>

    suspend fun getSubject(subjectId: Int): LiveData<SubjectEntity>

    suspend fun getSubjectWithSubjectDetails(semesterCode: Int): LiveData<List<SubjectWithSubjectDetails>>
}