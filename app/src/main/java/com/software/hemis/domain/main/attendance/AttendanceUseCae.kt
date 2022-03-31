package com.software.hemis.domain.main.attendance

import javax.inject.Inject

class AttendanceUseCae @Inject constructor(private val attendanceRepository: AttendanceRepository) {

    suspend fun attendance() = attendanceRepository.attendance()

    suspend fun insertAttendance(attendanceEntity: AttendanceEntity) =
        attendanceRepository.insertAttendance(attendanceEntity)

    suspend fun getAttendanceBySubject(subjectId: Int, semesterId: Int) =
        attendanceRepository.getAttendanceBySubject(subjectId, semesterId)

}