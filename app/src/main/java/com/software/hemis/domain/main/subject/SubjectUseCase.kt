package com.software.hemis.domain.main.subject

import javax.inject.Inject

class SubjectUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository,
) {
    suspend fun execute() =
        subjectRepository.subjects()

    suspend fun insertSubject(subjectEntity: SubjectEntity) =
        subjectRepository.insertSubject(subjectEntity)

    suspend fun getSubjectList(semesterCode: Int) = subjectRepository.getSubjectList(semesterCode)

    suspend fun getSubject(subjectId: Int) = subjectRepository.getSubject(subjectId)

    suspend fun getSubjectWithSubjectDetails(semesterId: Int) = subjectRepository.getSubjectWithSubjectDetails(semesterId)
}