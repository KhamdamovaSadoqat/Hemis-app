package com.software.hemis.domain.main.subjectDetails

import com.software.hemis.domain.main.task.TaskEntity
import javax.inject.Inject

class SubjectDetailsUseCase @Inject constructor(
    private val subjectDetailsRepository: SubjectDetailsRepository,
) {
    suspend fun subjectDetails(subject: Int, semester: Int) =
        subjectDetailsRepository.subjectDetails(subject, semester)

    suspend fun insertSubjectDetails(subjectDetailsEntity: SubjectDetailsEntity) =
        subjectDetailsRepository.insertSubjectDetails(subjectDetailsEntity)

    suspend fun getSubjectDetailsWithSubject(subjectId: Int) = subjectDetailsRepository.getSubjectDetailsWithSubject(subjectId)

    suspend fun insertTask(taskEntity: TaskEntity) = subjectDetailsRepository.insertTask(taskEntity)

    suspend fun getTask(subjectId: Int, taskStatusCode: Int, semesterId: Int) = subjectDetailsRepository.getTask(subjectId, taskStatusCode, semesterId)

    suspend fun getAllTask( semesterId: Int) = subjectDetailsRepository.getAllTask(semesterId)
}