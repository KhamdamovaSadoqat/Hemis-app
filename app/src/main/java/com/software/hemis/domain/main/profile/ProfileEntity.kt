package com.software.hemis.domain.main.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProfileEntity(

    val image: String,
    @PrimaryKey
    val studentIdNumber: String,
    val phone: String,

    val birthDate: Int,
    val secondName: String,
    val thirdName: String,
    val firstName: String,

    val levelCode: String,
    val levelName: String,

    val groupName: String,
    val groupId: Int,

    val semesterCurrent: Boolean,
    val semesterCode: String,
    val semesterName: String,
    val semesterId: Int,

    val educationYearName: String,
    val educationYearCode: String,
    val educationYearCurrent: Int,



)
