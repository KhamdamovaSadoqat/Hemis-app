package com.software.hemis.domain.main.semester

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SemesterEntity(

    @PrimaryKey
    val id: Int,
    val code: String,
    val name: String,
    val current: Boolean,

    val educationYearName: String,
    val educationYearCode: String,
    val educationYearCurrent: Boolean,
)