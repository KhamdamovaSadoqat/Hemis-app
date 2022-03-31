package com.software.hemis.data.main.taskUpload

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.TaskDao
import com.software.hemis.database.room.TaskDetailDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.base.BaseResultList
import com.software.hemis.domain.main.task.TaskDetailEntity
import com.software.hemis.domain.main.task.TaskDetailsRepository
import com.software.hemis.domain.main.task.TaskSentSubmissionEntity
import com.software.hemis.utils.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class TaskDetailsDetailsRepositoryImp @Inject constructor(
    private val taskDetailsApi: TaskDetailsApi,
    private val taskDetailDao: TaskDetailDao,
    private val taskDao: TaskDao,
) : TaskDetailsRepository {
    override suspend fun taskDetails(taskId: Int): Flow<BaseResultList<TaskDetailEntity, List<TaskSentSubmissionEntity>, WrappedResponse<TaskDetailsResponse>>> {
        return flow {
            val response =
                taskDetailsApi.taskDetails("${Constants.universityUrl}education/task-detail?task=$taskId")
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    val taskSentSubmissionList = arrayListOf<TaskSentSubmissionEntity>()
                    // checking if student sent any file to teacher or not
                    if (body.data!!.studentTaskActivities != null) {
                        for (task in body.data!!.studentTaskActivities!!.indices) {
                            val submittedFiles: HashMap<String?, String?> =
                                object : HashMap<String?, String?>() {}
                            if (body.data!!.studentTaskActivities!![task]?.files != null) {
                                //getting submitted files if exist
                                for (file in body.data!!.studentTaskActivities!![task]?.files!!.indices) {
                                    submittedFiles[body.data!!.studentTaskActivities!![task]?.files!![file]?.url] =
                                        body.data!!.studentTaskActivities!![task]?.files!![file]?.name
                                }
                            }

                            val sentSubmission = TaskSentSubmissionEntity(
                                body.data!!.studentTaskActivities!![task]?.id ?: -1,
                                body.data!!.studentTaskActivities!![task]?.sendDate ?: -1,
                                submittedFiles,
                                body.data!!.studentTaskActivities!![task]?.mark ?: -1,
                                body.data!!.studentTaskActivities!![task]?.markedComment ?: "",
                                body.data!!.studentTaskActivities!![task]?.markedDate ?: -1,
                                body.data!!.id ?: -1
                            )
                            taskSentSubmissionList.add(sentSubmission)
                        }
                    }
                    val files: HashMap<String?, String?> =
                        object : HashMap<String?, String?>() {}
                    if (body.data!!.files != null) {
                        //getting files if exist
                        //file which is given to student to do task
                        for (file in body.data!!.files!!.indices) {
                            files[body.data!!.files!![file]?.url] =
                                body.data!!.files!![file]?.name
                        }
                    }

                    val taskDetail = if (body.data!!.studentTaskActivities!!.isEmpty()) {
                        TaskDetailEntity(
                            body.data!!.id ?: -1,
                            files,
                            body.data!!.updatedAt ?: -1,
                            body.data!!.studentTaskActivity?.id ?: -1,
                            -1
                        )
                    } else TaskDetailEntity(
                        body.data!!.id ?: -1,
                        files,
                        body.data!!.updatedAt ?: -1,
                        body.data!!.studentTaskActivity?.id ?: -1,
                        body.data!!.studentTaskActivities!![0]?.id ?: 0
                    )
                    emit(BaseResultList.Success(taskDetail, taskSentSubmissionList))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<TaskDetailsResponse>>() {}.type
                val err: WrappedResponse<TaskDetailsResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResultList.Error(err))
            }
        }
    }

    override suspend fun taskUpload(taskId: Int, files: List<MultipartBody.Part>): Flow<BaseResult<Unit, WrappedResponse<TaskUploadResponse>>> {
        return flow {
            val response =
                taskDetailsApi.taskUpload("${Constants.universityUrl}education/task-upload?task=$taskId", files)
            if (response.isSuccessful ) {
                emit(BaseResult.Success(Unit))
            } else {
                val type = object : TypeToken<WrappedResponse<TaskUploadResponse>>() {}.type
                val err: WrappedResponse<TaskUploadResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun insertTaskDetails(taskDetailEntity: TaskDetailEntity) {
        taskDetailDao.insertTaskDetails(taskDetailEntity)
    }

    override suspend fun updateTask(taskId: Int, taskStatus: String, taskStatusCode: Int) {
        taskDetailDao.updateTask(taskId, taskStatus, taskStatusCode)
    }

    override suspend fun insertTaskSentSubmission(taskSentSubmissionEntity: TaskSentSubmissionEntity) {
        taskDetailDao.insertTaskSentSubmission(taskSentSubmissionEntity)
    }

    override suspend fun getTaskWithDetails(subjectId: Int, semesterId: Int, taskId: Int) {
        taskDao.getTaskWithDetails(subjectId, semesterId, taskId)
    }

    override suspend fun getTaskDetailsWithSubmission(taskId: Int) {
        taskDao.getTaskSentSubmission(taskId)
    }
}