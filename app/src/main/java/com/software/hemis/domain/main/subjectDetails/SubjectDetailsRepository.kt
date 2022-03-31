package com.software.hemis.domain.main.subjectDetails

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.subjectDetails.SubjectDetailsResponse
import com.software.hemis.domain.base.BaseResultList
import com.software.hemis.domain.main.task.TaskEntity
import com.software.hemis.domain.main.task.TaskWithSubject
import kotlinx.coroutines.flow.Flow

interface SubjectDetailsRepository {
    suspend fun subjectDetails(subject: Int, semester: Int):
            Flow<BaseResultList<SubjectDetailsEntity, List<TaskEntity>, WrappedResponse<SubjectDetailsResponse>>>

    suspend fun insertSubjectDetails(subjectDetailsEntity: SubjectDetailsEntity)

    suspend fun getSubjectDetailsWithSubject(subjectId: Int): LiveData<SubjectDetailsWithSubject>

    suspend fun insertTask(taskEntity: TaskEntity)

    suspend fun getTask(subjectId: Int, taskStatusCode: Int, semesterId: Int): LiveData<List<TaskWithSubject>>

    suspend fun getAllTaskBySubject(subjectId: Int, semesterId: Int): LiveData<List<TaskWithSubject>>

    suspend fun getAllTask(semesterId: Int): LiveData<List<TaskWithSubject>>
}