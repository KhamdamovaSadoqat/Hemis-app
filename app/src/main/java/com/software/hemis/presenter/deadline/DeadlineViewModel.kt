package com.software.hemis.presenter.deadline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.semester.SemesterResponse
import com.software.hemis.data.main.taskUpload.TaskDetailsResponse
import com.software.hemis.data.main.taskUpload.TaskUploadResponse
import com.software.hemis.database.room.SubjectDao
import com.software.hemis.database.room.TaskDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.base.BaseResultList
import com.software.hemis.domain.main.semester.SemesterEntity
import com.software.hemis.domain.main.semester.WeekEntity
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.task.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DeadlineViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val subjectDao: SubjectDao,
    private val taskDetailsUseCase: TaskDetailsUseCase
) : ViewModel() {

    private val taskUploadState = MutableStateFlow<TaskUploadState>(TaskUploadState.Init)
    val mTaskUploadState: StateFlow<TaskUploadState> get() = taskUploadState

    private val taskDetailsState = MutableStateFlow<TaskDetailsState>(TaskDetailsState.Init)

    fun taskUpload(taskId: Int, files: List<MultipartBody.Part>) {
        Log.d("DeadlineViewModel", "------------- taskUpload: ")
        viewModelScope.async {
            taskDetailsUseCase.taskUpload(taskId, files)
                .onStart {
                    taskUploadState.value = TaskUploadState.IsLoading(true)
                }
                .catch { exception ->
                    Log.d("DeadlineViewModel",
                        "------------- taskUpload: catch: ${exception.message}")
                    Log.d("DeadlineViewModel",
                        "------------- taskUpload: catch: ${exception.cause?.message}")
                    taskUploadState.value = TaskUploadState.IsLoading(false)
                    taskUploadState.value = TaskUploadState.ShowToast(exception.message.toString())
                }
                .collect { result ->
                    taskUploadState.value = TaskUploadState.IsLoading(false)
                    when (result) {
                        is BaseResult.Success -> taskUploadState.value =
                            TaskUploadState.SuccessTaskUpload("Success")
                        is BaseResult.Error -> taskUploadState.value =
                            TaskUploadState.ErrorTaskUpload(result.rawResponse)
                    }
                }
        }.onAwait
    }

    fun getTaskDetail(taskId: Int) {
        viewModelScope.async {
            taskDetailsUseCase.taskDetails(taskId)
                .onStart {
                    taskDetailsState.value = TaskDetailsState.IsLoading(true)
                }.catch { exception ->
                    taskDetailsState.value = TaskDetailsState.IsLoading(false)
                    taskDetailsState.value =
                        TaskDetailsState.ShowToast(exception.cause?.message.toString())
                }.collect { result ->
                    taskDetailsState.value = TaskDetailsState.IsLoading(false)
                    when (result) {
                        is BaseResultList.Success -> {
                            updateTask(result.data.id, "Topshirdi", 12)
                            insertTaskDetails(result.data)
                            result.data2.forEach {
                                insertTaskSentSubmission(it)
                            }
                        }
                        is BaseResultList.Error -> taskDetailsState.value =
                            TaskDetailsState.ErrorTaskDetails(result.rawResponse)
                    }
                }
        }.onAwait
    }

    fun getAllDeadlineTask(taskStatusCode: Int, semesterId: Int): LiveData<List<TaskWithSubject>> {
        return taskDao.getAllDeadlineTask(taskStatusCode, semesterId)
    }

    fun getAllTask(semesterId: Int): LiveData<List<TaskWithSubject>>{
        return taskDao.getAllTask(semesterId)
    }

    fun getTaskWithDetail(
        subjectId: Int,
        semesterId: Int,
        taskId: Int,
    ): LiveData<TaskWithTaskDetail> {
        return taskDao.getTaskWithDetails(subjectId, semesterId, taskId)
    }

    fun getSubject(subjectId: Int): LiveData<SubjectEntity> {
        return subjectDao.getSubject(subjectId)
    }

    fun getTaskDetailsWithSubmission(taskId: Int): LiveData<TaskSentSubmissionEntity> {
        return  taskDao.getTaskSentSubmission(taskId)
    }

    private fun insertTaskDetails(taskDetailsEntity: TaskDetailEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDetailsUseCase.insertTaskDetails(taskDetailsEntity)
            }
        }
    }

    private fun updateTask(taskId: Int, taskStatus: String, taskStatusCode: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDetailsUseCase.updateTask(taskId, taskStatus, taskStatusCode)
            }
        }
    }

    private fun insertTaskSentSubmission(taskSentSubmissionEntity: TaskSentSubmissionEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDetailsUseCase.insertTaskSentSubmission(taskSentSubmissionEntity)
            }
        }
    }

}

sealed class TaskUploadState{
    object Init : TaskUploadState()
    data class IsLoading(val isLoading: Boolean) : TaskUploadState()
    data class ShowToast(val message: String) : TaskUploadState()
    data class SuccessTaskUpload(val message: String) : TaskUploadState()
    data class ErrorTaskUpload(val rawResponse: WrappedResponse<TaskUploadResponse>) : TaskUploadState()
}

sealed class SemesterState {
    object Init : SemesterState()
    data class IsLoading(val isLoading: Boolean) : SemesterState()
    data class ShowToast(val message: String) : SemesterState()
    data class SuccessSemester(
        val semesterEntity: List<SemesterEntity>,
        val weekEntity: List<WeekEntity>,
    ) : SemesterState()

    data class ErrorSemester(val rawResponse: WrappedResponse<SemesterResponse>) : SemesterState()
}

sealed class TaskDetailsState {
    object Init : TaskDetailsState()
    data class IsLoading(val isLoading: Boolean) : TaskDetailsState()
    data class ShowToast(val message: String) : TaskDetailsState()
    data class SuccessTaskDetails(
        val taskDetailsEntity: TaskDetailEntity,
        val taskSentSubmissionEntity: List<TaskSentSubmissionEntity>,
    ) : TaskDetailsState()

    data class ErrorTaskDetails(val rawResponse: WrappedResponse<TaskDetailsResponse>) :
        TaskDetailsState()
}


