package com.software.hemis.domain.main.task

import androidx.room.Embedded
import androidx.room.Relation
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.task.TaskEntity

data class TaskWithSubject(
    @Embedded
    val taskEntity: TaskEntity,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "subjectId"
    )
    val subject: SubjectEntity,
)
