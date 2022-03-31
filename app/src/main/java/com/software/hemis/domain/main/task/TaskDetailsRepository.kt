package com.software.hemis.domain.main.task

import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.taskUpload.TaskDetailsResponse
import com.software.hemis.data.main.taskUpload.TaskUploadResponse
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.base.BaseResultList
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface TaskDetailsRepository {

    suspend fun taskDetails(taskId: Int):
            Flow<BaseResultList<TaskDetailEntity, List<TaskSentSubmissionEntity>, WrappedResponse<TaskDetailsResponse>>>

    suspend fun taskUpload(taskId: Int, files: List<MultipartBody.Part>): Flow<BaseResult<Unit, WrappedResponse<TaskUploadResponse>>>

    suspend fun insertTaskDetails(taskDetailEntity: TaskDetailEntity)

    suspend fun updateTask(taskId: Int, taskStatus: String, taskStatusCode: Int)

    suspend fun insertTaskSentSubmission(taskSentSubmissionEntity: TaskSentSubmissionEntity)

    suspend fun getTaskWithDetails(subjectId: Int, semesterId: Int, taskId: Int)

    suspend fun getTaskDetailsWithSubmission(taskId: Int)

}
