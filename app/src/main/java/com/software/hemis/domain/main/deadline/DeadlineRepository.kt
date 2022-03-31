package com.software.hemis.domain.main.deadline

import java.util.concurrent.Flow

interface DeadlineRepository {

    suspend fun getAllDeadlineTask(taskStatusCode: Int)
}