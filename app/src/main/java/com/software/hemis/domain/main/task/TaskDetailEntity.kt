package com.software.hemis.domain.main.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskDetailEntity(
    @PrimaryKey
    val id: Int,
    val fileUrl: HashMap<String?, String?>,
    val updateAt: Int,
    val markedTaskId: Int,
    val submittedTaskId: Int
)