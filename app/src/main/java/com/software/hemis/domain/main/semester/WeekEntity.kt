package com.software.hemis.domain.main.semester

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeekEntity(

    @PrimaryKey
    val id: Int,
    val semesterId: String,
    val current: Boolean,
    val startTime: Int,
    val endTime: Int
)