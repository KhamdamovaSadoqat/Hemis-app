package com.software.hemis.domain.main.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskSentSubmissionEntity(
    @PrimaryKey
    val id: Int,
    val sentDate: Int,
    val file: HashMap<String?, String?>,
    val mark: Int,
    val markComment: String,
    val markedDate: Int,
    val taskDetailId: Int
)
