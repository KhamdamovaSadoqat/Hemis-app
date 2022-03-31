package com.software.hemis.presenter.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.attendance.AttendanceResponse
import com.software.hemis.data.main.resources.ResourceResponse
import com.software.hemis.data.main.subjectDetails.SubjectDetailsResponse
import com.software.hemis.data.main.subjects.SubjectResponse
import com.software.hemis.database.room.*
import com.software.hemis.domain.main.attendance.AttendanceEntity
import com.software.hemis.domain.main.resources.ResourceEntity
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.subject.SubjectWithSubjectDetails
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsEntity
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsWithSubject
import com.software.hemis.domain.main.task.TaskEntity
import com.software.hemis.domain.main.task.TaskWithSubject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectDao: SubjectDao,
    private val subjectDetailsDao: SubjectDetailsDao,
    private val taskDao: TaskDao,
    private val resourceDao: ResourceDao,
    private val attendanceDao: AttendanceDao
) : ViewModel() {

    fun getSubject(semesterCode: Int): LiveData<List<SubjectEntity>> {
        return subjectDao.getSubjectList(semesterCode)
    }

    fun getSubjectWithSubjectDetails(semesterCode: Int): LiveData<List<SubjectWithSubjectDetails>> {
        return subjectDao.getSubjectWithSubjectDetails(semesterCode)
    }

    fun getSubjectDetailsWithSubject(subjectId: Int): LiveData<SubjectDetailsWithSubject> {
        return subjectDetailsDao.getSubjectDetailsWithSubject(subjectId)
    }

    fun getTask(subjectId: Int, taskStatusCode: Int, semesterId: Int): LiveData<List<TaskWithSubject>> {
        return taskDao.getTask(subjectId, taskStatusCode, semesterId)
    }

    fun getAllTask(subjectId: Int, semesterId: Int): LiveData<List<TaskWithSubject>> {
        return taskDao.getAllTaskBySubject(subjectId, semesterId)
    }

    fun getResource(subjectId: Int, semesterId: Int, typeId: Int): LiveData<List<ResourceEntity>>{
        return  resourceDao.getResource(subjectId, semesterId, typeId)
    }

    fun getAttendance(subjectId: Int, semesterId: Int):LiveData<List<AttendanceEntity>>{
        return  attendanceDao.getAttendanceBySubject(subjectId, semesterId)
    }
}

sealed class SubjectState {
    object Init : SubjectState()
    data class IsLoading(val isLoading: Boolean) : SubjectState()
    data class ShowToast(val message: String) : SubjectState()
    data class SuccessSubject(val subjectEntity: List<SubjectEntity>) : SubjectState()
    data class ErrorLogin(val rawResponse: WrappedResponse<SubjectResponse>) : SubjectState()
}

sealed class SubjectDetailsState {
    object Init : SubjectDetailsState()
    data class IsLoading(val isLoading: Boolean) : SubjectDetailsState()
    data class ShowToast(val message: String) : SubjectDetailsState()
    data class SuccessSubjectDetails(
        val subjectDetailsEntity: SubjectDetailsEntity,
        val taskEntity: List<TaskEntity>,
    ) : SubjectDetailsState()

    data class ErrorSubjectDetails(val rawResponse: WrappedResponse<SubjectDetailsResponse>) :
        SubjectDetailsState()
}

sealed class ResourceState {
    object Init : ResourceState()
    data class IsLoading(val isLoading: Boolean) : ResourceState()
    data class ShowToast(val message: String) : ResourceState()
    data class SuccessResource(val resourceEntity: List<ResourceEntity>) : ResourceState()
    data class ErrorResource(val rawResponse: WrappedResponse<ResourceResponse>) : ResourceState()
}

sealed class AttendanceState {
    object Init : AttendanceState()
    data class IsLoading(val isLoading: Boolean) : AttendanceState()
    data class ShowToast(val message: String) : AttendanceState()
    data class SuccessAttendance(val resourceEntity: List<AttendanceEntity>) : AttendanceState()
    data class ErrorAttendance(val rawResponse: WrappedResponse<AttendanceResponse>) : AttendanceState()
}
