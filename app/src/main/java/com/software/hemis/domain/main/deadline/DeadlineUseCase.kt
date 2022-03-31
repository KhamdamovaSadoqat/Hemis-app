package com.software.hemis.domain.main.deadline

import javax.inject.Inject

class DeadlineUseCase @Inject constructor(
    private val deadlineRepository: DeadlineRepository
) {

    suspend fun getAllDeadlineTask(taskStatusCode: Int) = deadlineRepository.getAllDeadlineTask(taskStatusCode)
}