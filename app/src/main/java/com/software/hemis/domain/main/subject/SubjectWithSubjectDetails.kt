package com.software.hemis.domain.main.subject

import androidx.room.Embedded
import androidx.room.Relation
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsEntity

data class SubjectWithSubjectDetails(
    @Embedded
    val subject: SubjectEntity,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "id"
    )
    val subjectDetails: SubjectDetailsEntity
)
