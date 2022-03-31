package com.software.hemis.data.main.subjectDetails

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.SubjectDetailsDao
import com.software.hemis.database.room.TaskDao
import com.software.hemis.domain.base.BaseResultList
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsEntity
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsRepository
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsWithSubject
import com.software.hemis.domain.main.task.TaskEntity
import com.software.hemis.domain.main.task.TaskWithSubject
import com.software.hemis.utils.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubjectDetailsRepositoryImp @Inject constructor(
    private val subjectDetailsApi: SubjectDetailsApi,
    private val subjectDetailsDao: SubjectDetailsDao,
    private val taskDao: TaskDao
) : SubjectDetailsRepository {
    override suspend fun subjectDetails(
        subject: Int,
        semester: Int,
    ): Flow<BaseResultList<SubjectDetailsEntity, List<TaskEntity>, WrappedResponse<SubjectDetailsResponse>>> {
        return flow {
            val response = subjectDetailsApi.subjectDetails("${Constants.universityUrl}education/subject?subject=$subject&semester=$semester")
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    val taskList = arrayListOf<TaskEntity>()
                    // checking for task list: is given or not yet
                    if (body.data!!.tasks != null) {
                        for (task in body.data!!.tasks!!.indices) {
                            val task = TaskEntity(
                                body.data!!.tasks!![task]?.id ?: -1,
                                body.data!!.tasks!![task]?.name ?: "",
                                body.data!!.tasks!![task]?.deadline ?: -1,
                                body.data!!.tasks!![task]?.taskStatus?.name ?: "",
                                body.data!!.tasks!![task]?.taskStatus?.code?.toInt() ?: -1,
                                body.data!!.tasks!![task]?.trainingType?.name ?: "",
                                body.data!!.tasks!![task]?.attemptCount ?: 0,
                                body.data!!.tasks!![task]?.attemptLimit ?: -1,
                                body.data!!.tasks!![task]?.employee?.name ?: "",
                                body.data!!.tasks!![task]?.maxBall ?: -1,
                                body.data!!.tasks!![task]?.taskType?.name ?: "",
                                body.data!!.tasks!![task]?.comment ?: "",
                                body.data!!.subject?.id ?: -1,
                                semester
                            )
                            taskList.add(task)
                        }
                    }
                    val subjectDetail = SubjectDetailsEntity(
                        body.data!!.subject?.name ?: "",
                        body.data!!.subject?.id ?: -1,
                        body.data!!.subjectBall ?: -1,
                        body.data!!.maxBall ?: -1,
                        body.data!!.tasksCount ?: -1,
                        body.data!!.submitsCount ?: -1,
                        body.data!!.markedCount ?: -1,
                        body.data!!.resourcesCount ?: -1,
                        body.data!!.absentCount ?: -1
                    )
                    emit(BaseResultList.Success(subjectDetail, taskList))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<SubjectDetailsResponse>>() {}.type
                val err: WrappedResponse<SubjectDetailsResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResultList.Error(err))
            }
        }
    }

    override suspend fun insertSubjectDetails(subjectDetailsEntity: SubjectDetailsEntity) {
        subjectDetailsDao.insertSubjectDetails(subjectDetailsEntity)
    }

    override suspend fun getSubjectDetailsWithSubject(subjectId: Int): LiveData<SubjectDetailsWithSubject> {
        return subjectDetailsDao.getSubjectDetailsWithSubject(subjectId)
    }

    override suspend fun insertTask(taskEntity: TaskEntity) {
        taskDao.insertTask(taskEntity)
    }

    override suspend fun getTask(subjectId: Int, taskStatusCode: Int, semesterId: Int): LiveData<List<TaskWithSubject>> {
        return taskDao.getTask(subjectId, taskStatusCode, semesterId)
    }

    override suspend fun getAllTaskBySubject(subjectId: Int, semesterId: Int): LiveData<List<TaskWithSubject>> {
        return taskDao.getAllTaskBySubject(subjectId, semesterId)
    }

    override suspend fun getAllTask(semesterId: Int): LiveData<List<TaskWithSubject>> {
        return taskDao.getAllTask(semesterId)
    }
}