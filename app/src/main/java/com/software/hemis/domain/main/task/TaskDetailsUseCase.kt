package com.software.hemis.domain.main.task

import okhttp3.MultipartBody
import javax.inject.Inject

class TaskDetailsUseCase @Inject constructor(
    private val taskDetailsRepository: TaskDetailsRepository
) {
    suspend fun taskDetails(taskId: Int) = taskDetailsRepository.taskDetails(taskId)

    suspend fun taskUpload(taskId: Int, files: List<MultipartBody.Part>) = taskDetailsRepository.taskUpload(taskId, files)

    suspend fun insertTaskDetails(taskDetailEntity: TaskDetailEntity) =
        taskDetailsRepository.insertTaskDetails(taskDetailEntity)

    suspend fun updateTask(taskId: Int, taskStatus: String, taskStatusCode: Int) = taskDetailsRepository.updateTask(taskId, taskStatus, taskStatusCode)

    suspend fun insertTaskSentSubmission(taskSentSubmissionEntity: TaskSentSubmissionEntity) =
        taskDetailsRepository.insertTaskSentSubmission(taskSentSubmissionEntity)

    suspend fun getTaskWithDetail(subjectId: Int, semesterId: Int, taskId: Int) =
        taskDetailsRepository.getTaskWithDetails(subjectId, semesterId, taskId)

    suspend fun getTaskDetailsWithSubmission(taskId: Int) = taskDetailsRepository.getTaskDetailsWithSubmission(taskId)


}