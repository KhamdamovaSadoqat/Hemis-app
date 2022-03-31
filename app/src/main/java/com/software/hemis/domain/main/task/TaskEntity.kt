package com.software.hemis.domain.main.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(

    @PrimaryKey
    val id: Int,
    val name: String,
    val deadline: Int,
    val taskStatus: String,
    val taskStatusCode: Int,
    val trainingType: String,
    val attemptCount: Int,
    val attemptLimit: Int,
    val employee: String,
    val maxBall: Int,
    val taskType: String,
    val comment: String,
    val subjectId: Int,
    val semesterId: Int,
)
