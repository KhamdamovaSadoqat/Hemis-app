package com.software.hemis.domain.main.subjectDetails

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectDetailsEntity(

    val name: String,
    @PrimaryKey
    val id: Int,
    val ball: Int,
    val maxBall: Int,
    val tasksCount: Int,
    val submitsCount: Int,
    val markedCount: Int,
    val resourcesCount: Int,
    val absentCount: Int,
)

