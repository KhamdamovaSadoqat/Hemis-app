package com.software.hemis.domain.main.subject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectEntity(

    val subjectName: String,
    @PrimaryKey
    val subjectId: Int,
    val subjectTypeName: String,
    val subjectTypeCode: String,
    val semesterCode: Int,
    val totalAcload: Int,
    val credit: Int,
)

