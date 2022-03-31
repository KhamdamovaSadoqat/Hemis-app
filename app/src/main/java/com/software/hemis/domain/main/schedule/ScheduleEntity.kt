package com.software.hemis.domain.main.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduleEntity(

    val subjectName: String,
    val subjectId: Int,
    val weekId: Int,
    val auditoriumName: String,
    val auditoriumCode: Int,
    val trainingName: String,
    val trainingCode: String,
    val lessonDate: Int,
    val lessonPairCode: String,
    val lessonPairName: String,
    val lessonPairStartTime: String,
    val employeeName: String,
    val employeeId: Int,
    val additional: String,
    @PrimaryKey
    val key: String,
)
