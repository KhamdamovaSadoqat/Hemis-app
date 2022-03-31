package com.software.hemis.domain.main.subjectDetails

import androidx.room.Embedded
import androidx.room.Relation
import com.software.hemis.domain.main.subject.SubjectEntity

data class SubjectDetailsWithSubject(
    @Embedded
    val subjectDetails: SubjectDetailsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val subject: SubjectEntity
)
