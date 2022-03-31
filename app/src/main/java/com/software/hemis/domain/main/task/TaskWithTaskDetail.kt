package com.software.hemis.domain.main.task

import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation

data class TaskWithTaskDetail(

    @Embedded
    val taskEntity: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val taskDetailEntity: TaskDetailEntity,

)