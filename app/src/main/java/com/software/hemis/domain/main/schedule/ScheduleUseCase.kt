package com.software.hemis.domain.main.schedule

import javax.inject.Inject

class ScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend fun execute(week: Int) = scheduleRepository.schedule(week)

    suspend fun insertSchedule(scheduleEntity: ScheduleEntity) =
        scheduleRepository.insertSchedule(scheduleEntity)

    suspend fun getSemester(day: Int) = scheduleRepository.getSchedule(day)

    suspend fun deleteSchedule() = scheduleRepository.deleteSchedule()
}