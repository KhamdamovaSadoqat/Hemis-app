package com.software.hemis.presenter.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.hemis.data.auth.login.LoginRequest
import com.software.hemis.data.auth.login.LoginResponse
import com.software.hemis.data.auth.university.UniversityResponse
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.domain.auth.login.LoginUseCase
import com.software.hemis.domain.auth.university.UniversityEntity
import com.software.hemis.domain.auth.university.UniversityUseCase
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.base.BaseResultList
import com.software.hemis.domain.main.attendance.AttendanceEntity
import com.software.hemis.domain.main.attendance.AttendanceUseCae
import com.software.hemis.domain.main.profile.ProfileEntity
import com.software.hemis.domain.main.profile.ProfileUseCase
import com.software.hemis.domain.main.resources.ResourceEntity
import com.software.hemis.domain.main.resources.ResourceUseCase
import com.software.hemis.domain.main.schedule.ScheduleEntity
import com.software.hemis.domain.main.schedule.ScheduleUseCase
import com.software.hemis.domain.main.semester.SemesterEntity
import com.software.hemis.domain.main.semester.SemesterUseCase
import com.software.hemis.domain.main.semester.WeekEntity
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.subject.SubjectUseCase
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsEntity
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsUseCase
import com.software.hemis.domain.main.task.TaskDetailEntity
import com.software.hemis.domain.main.task.TaskDetailsUseCase
import com.software.hemis.domain.main.task.TaskEntity
import com.software.hemis.domain.main.task.TaskSentSubmissionEntity
import com.software.hemis.presenter.deadline.SemesterState
import com.software.hemis.presenter.deadline.TaskDetailsState
import com.software.hemis.presenter.other.data.profile.ProfileState
import com.software.hemis.presenter.schedule.ScheduleState
import com.software.hemis.presenter.subject.AttendanceState
import com.software.hemis.presenter.subject.ResourceState
import com.software.hemis.presenter.subject.SubjectDetailsState
import com.software.hemis.presenter.subject.SubjectState
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val universityUseCase: UniversityUseCase,
    private val loginUseCase: LoginUseCase,
    private val semesterUseCase: SemesterUseCase,
    private val scheduleUseCase: ScheduleUseCase,
    private val subjectUseCase: SubjectUseCase,
    private val subjectDetailsUseCase: SubjectDetailsUseCase,
    private val taskDetailsUseCase: TaskDetailsUseCase,
    private val profileUseCase: ProfileUseCase,
    private val resourceUseCase: ResourceUseCase,
    private val attendanceUseCae: AttendanceUseCae,
    private val prefs: SharedPref
) : ViewModel() {

    val mutableLiveData = MutableLiveData<Boolean>()

    private val universityState = MutableStateFlow<UniversityState>(UniversityState.Init)
    val mUniversityState: StateFlow<UniversityState> get() = universityState

    private val loginState = MutableStateFlow<LoginState>(LoginState.Init)
    val mLoginState: StateFlow<LoginState> get() = loginState

    private val semesterState = MutableStateFlow<SemesterState>(SemesterState.Init)
    private val scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Init)
    private val subjectState = MutableStateFlow<SubjectState>(SubjectState.Init)
    private val subjectDetailsState =
        MutableStateFlow<SubjectDetailsState>(SubjectDetailsState.Init)
    private val taskDetailsState = MutableStateFlow<TaskDetailsState>(TaskDetailsState.Init)
    private val resourceState = MutableStateFlow<ResourceState>(ResourceState.Init)
    private val profileState = MutableStateFlow<ProfileState>(ProfileState.Init)
    private val attendanceState = MutableStateFlow<AttendanceState>(AttendanceState.Init)

    fun university() {
        viewModelScope.async {
            universityUseCase.university()
                .onStart {
                    universityState.value = UniversityState.IsLoading(true)
                }
                .catch { exception ->
                    universityState.value = UniversityState.IsLoading(false)
                    universityState.value = UniversityState.ShowToast(exception.message.toString())
                }
                .collect { result ->
                    universityState.value = UniversityState.IsLoading(false)
                    when (result) {
                        is BaseResult.Success -> universityState.value =
                            UniversityState.SuccessUniversity(result.data)
                        is BaseResult.Error -> universityState.value =
                            UniversityState.ErrorUniversity(result.rawResponse)
                    }
                }
        }.onAwait
    }

    fun login(url:String,loginRequest: LoginRequest) {
        viewModelScope.async {
            loginUseCase.execute(url,loginRequest)
                .onStart {
                    loginState.value = LoginState.IsLoading(true)
                }
                .catch { exception ->
                    loginState.value = LoginState.IsLoading(false)
                    loginState.value = LoginState.ShowToast(exception.message.toString())
                }
                .collect { result ->
                    loginState.value = LoginState.IsLoading(false)
                    when (result) {
                        is BaseResult.Success -> loginState.value =
                            LoginState.SuccessLogin("Success")
                        is BaseResult.Error -> loginState.value =
                            LoginState.ErrorLogin(result.rawResponse)
                    }
                }
        }.onAwait
    }

    fun requestAll() {
        viewModelScope.launch {
            val list = mutableListOf<Deferred<Boolean>>()
            coroutineScope {
                val semester = async { getSemester() }
                list.add(semester)
                val subject = async { getSubject() }
                list.add(subject)
                val profile = async { getProfile() }
                list.add(profile)
                val attendance = async { getAttendance() }
                list.add(attendance)
            }
            list.awaitAll()
            mutableLiveData.value = true
        }
    }

    private suspend fun getSemester(): Boolean {
        semesterUseCase.execute()
            .onStart {
                semesterState.value = SemesterState.IsLoading(true)
            }.catch { exception ->
                semesterState.value = SemesterState.IsLoading(false)
                semesterState.value = SemesterState.ShowToast(exception.message.toString())
            }.collect { result ->
                semesterState.value = SemesterState.IsLoading(false)
                when (result) {
                    is BaseResultList.Success -> {
                        result.data.forEach { insertSemester(it) }
                        result.data2.forEach {
                            insertWeek(it)
                            getSchedule(it.id)
                        }
                    }
                    is BaseResultList.Error -> semesterState.value =
                        SemesterState.ErrorSemester(result.rawResponse)
                }
            }
        return true
    }

    private suspend fun getSchedule(weekId: Int) {
        scheduleUseCase.execute(weekId)
            .onStart {
                scheduleState.value = ScheduleState.IsLoading(true)
            }.catch { exception ->
                scheduleState.value = ScheduleState.IsLoading(false)
                scheduleState.value = ScheduleState.ShowToast(exception.message.toString())
            }.collect { result ->
                scheduleState.value = ScheduleState.IsLoading(false)
                when (result) {
                    is BaseResult.Success -> {
                        result.data.forEach { insertSchedule(it) }
                    }
                    is BaseResult.Error -> scheduleState.value =
                        ScheduleState.ErrorSchedule(result.rawResponse)
                }
            }
    }

    private suspend fun getSubject(): Boolean {
        subjectUseCase.execute()
            .onStart {
                subjectState.value = SubjectState.IsLoading(true)
            }.catch { exception ->
                subjectState.value = SubjectState.IsLoading(false)
                subjectState.value = SubjectState.ShowToast(exception.message.toString())
            }.collect { result ->
                subjectState.value = SubjectState.IsLoading(false)
                when (result) {
                    is BaseResult.Success -> {
                        result.data.forEach {
                            insertSubject(it)
                            getSubjectDetail(it.subjectId, it.semesterCode)
                            getResource(it.subjectId, it.semesterCode)
                        }
                    }
                    is BaseResult.Error -> subjectState.value =
                        SubjectState.ErrorLogin(result.rawResponse)
                }
            }
        return true
    }

    private suspend fun getSubjectDetail(subjectId: Int, semesterId: Int) {
        subjectDetailsUseCase.subjectDetails(subjectId, semesterId)
            .onStart {
                subjectDetailsState.value = SubjectDetailsState.IsLoading(true)
            }.catch { exception ->
                subjectDetailsState.value = SubjectDetailsState.IsLoading(false)
                subjectDetailsState.value =
                    SubjectDetailsState.ShowToast(exception.cause?.message.toString())
            }.collect { result ->
                subjectDetailsState.value = SubjectDetailsState.IsLoading(false)
                when (result) {
                    is BaseResultList.Success -> {
                        insertSubjectDetails(result.data)
                        result.data2.forEach {
                            insertTask(it)
                            getTaskDetail(it.id)
                        }
                    }
                    is BaseResultList.Error ->
                        subjectDetailsState.value =
                            SubjectDetailsState.ErrorSubjectDetails(result.rawResponse)
                }
            }
    }

    private suspend fun getTaskDetail(taskId: Int) {
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
                        insertTaskDetails(result.data)
                        result.data2.forEach {
                            insertTaskSentSubmission(it)
                        }
                    }
                    is BaseResultList.Error -> taskDetailsState.value =
                        TaskDetailsState.ErrorTaskDetails(result.rawResponse)
                }
            }
    }

    private suspend fun getResource(subjectId: Int, semesterId: Int){
        resourceUseCase.resource(subjectId, semesterId)
            .onStart {
                resourceState.value = ResourceState.IsLoading(true)
            }.catch { exception ->
                resourceState.value = ResourceState.IsLoading(false)
                resourceState.value =
                    ResourceState.ShowToast(exception.cause?.message.toString())
            }.collect { result ->
                resourceState.value = ResourceState.IsLoading(false)
                when (result) {
                    is BaseResult.Success -> {
                        Log.d("LoginViewModel", "------------- getResource: collect")
                        result.data.forEach {
                            insertResource(it)
                        }
                    }
                    is BaseResult.Error -> resourceState.value =
                        ResourceState.ErrorResource(result.rawResponse)
                }
            }
    }

    private suspend fun getProfile(): Boolean {
        profileUseCase.execute()
            .onStart {
                profileState.value = ProfileState.IsLoading(true)
            }.catch { exception ->
                profileState.value = ProfileState.IsLoading(false)
                profileState.value = ProfileState.ShowToast(exception.cause?.message.toString())
            }
            .collect { result ->
                profileState.value = ProfileState.IsLoading(false)
                when (result) {
                    is BaseResult.Success -> {
                        insertProfile(result.data)
                    }
                    is BaseResult.Error ->
                        profileState.value =
                            ProfileState.ErrorProfile(result.rawResponse)
                }
            }
        return true
    }

    private suspend fun getAttendance(): Boolean{
        attendanceUseCae.attendance()
            .onStart {
                attendanceState.value = AttendanceState.IsLoading(true)
            }.catch { exception ->
                attendanceState.value = AttendanceState.IsLoading(false)
                attendanceState.value = AttendanceState.ShowToast(exception.cause?.message.toString())
            }
            .collect { result ->
                attendanceState.value = AttendanceState.IsLoading(false)
                when (result) {
                    is BaseResult.Success -> {
                        result.data.forEach {
                            insertAttendance(it)
                        }
                    }
                    is BaseResult.Error ->
                        attendanceState.value =
                            AttendanceState.ErrorAttendance(result.rawResponse)
                }
            }
        return true
    }

    private fun insertSemester(semesterEntity: SemesterEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                semesterUseCase.insertSemester(semesterEntity)
            }
        }
    }

    private fun insertWeek(weekEntity: WeekEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                semesterUseCase.insertWeek(weekEntity)
            }
        }
    }

    private fun insertSchedule(scheduleEntity: ScheduleEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                scheduleUseCase.insertSchedule(scheduleEntity)
            }
        }
    }

    private fun insertSubject(subjectEntity: SubjectEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                subjectUseCase.insertSubject(subjectEntity)
            }
        }
    }

    private fun insertSubjectDetails(subjectDetailsEntity: SubjectDetailsEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                subjectDetailsUseCase.insertSubjectDetails(subjectDetailsEntity)
            }
        }
    }

    private fun insertTask(taskEntity: TaskEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                subjectDetailsUseCase.insertTask(taskEntity)
            }
        }
    }

    private fun insertTaskDetails(taskDetailsEntity: TaskDetailEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDetailsUseCase.insertTaskDetails(taskDetailsEntity)
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

    private fun insertProfile(profileEntity: ProfileEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                profileUseCase.insertProfile(profileEntity)
            }
        }
    }

    private fun insertResource(resource: ResourceEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                resourceUseCase.insertResource(resource)
            }
        }
    }

    private fun insertAttendance(attendance: AttendanceEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                attendanceUseCae.insertAttendance(attendance)
            }
        }
    }

}

sealed class UniversityState {
    object Init : UniversityState()
    data class IsLoading(val isLoading: Boolean) : UniversityState()
    data class ShowToast(val message: String) : UniversityState()
    data class SuccessUniversity(val university: List<UniversityEntity>) : UniversityState()
    data class ErrorUniversity(val rawResponse: WrappedResponse<UniversityResponse>) :
        UniversityState()
}

sealed class LoginState {
    object Init : LoginState()
    data class IsLoading(val isLoading: Boolean) : LoginState()
    data class ShowToast(val message: String) : LoginState()
    data class SuccessLogin(val loginEntity: String) : LoginState()
    data class ErrorLogin(val rawResponse: WrappedResponse<LoginResponse>) : LoginState()
}