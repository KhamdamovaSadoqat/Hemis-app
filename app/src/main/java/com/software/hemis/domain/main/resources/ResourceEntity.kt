package com.software.hemis.domain.main.resources

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResourceEntity(

    @PrimaryKey
    val id: Int,
    val name: String,
    val comment: String,
    val trainingTypeCode: String,
    val trainingTypeName: String,
    val employeeId: Int,
    val employeeName: String,
    val file: HashMap<String?, String?>,
    val updateAt: Int,
    val subjectId: Int,
    val semesterId: Int,
)
