package com.software.hemis.domain.main.semester

import javax.inject.Inject

class SemesterUseCase @Inject constructor(
    private val semesterRepository: SemesterRepository,
) {
    suspend fun execute() = semesterRepository.semester()

    suspend fun insertSemester(semesterEntity: SemesterEntity) =
        semesterRepository.insertSemester(semesterEntity)

    suspend fun insertWeek(weekEntity: WeekEntity) = semesterRepository.insertWeek(weekEntity)

    suspend fun getSemester() = semesterRepository.getSemester()

    suspend fun getCurrentSemester(currentSemester: Int) = semesterRepository.getCurrentSemester(currentSemester)

}