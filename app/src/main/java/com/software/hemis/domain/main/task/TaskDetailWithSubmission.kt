package com.software.hemis.domain.main.task

import androidx.room.Embedded
import androidx.room.Relation

data class TaskDetailWithSubmission(
    @Embedded
    val taskDetailEntity: TaskDetailEntity,
    @Relation(
        parentColumn = "submittedTaskId",
        entityColumn = "id"
    )
    val taskSentSubmissionEntity: TaskSentSubmissionEntity
)
