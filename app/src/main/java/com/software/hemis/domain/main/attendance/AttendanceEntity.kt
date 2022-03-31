package com.software.hemis.domain.main.attendance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttendanceEntity(


    val subjectId: Int,
    val semesterId: Int,
    val subjectName: String,
    val trainingTypeCode: Int,
    val trainingTypeName: String,
    val lessonPairCode: Int,
    val lessonPairName: String,
    val lessonPairStartTime: String,
    val lessonPairEndTime: String,
    val absentOff: Int,
    val absentOn: Int,
    val lessonDate: Int,
    @PrimaryKey
    val id: String,
    )
