package com.software.hemis.presenter.other.data.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.profile.PasswordChangeRequest
import com.software.hemis.data.main.profile.PasswordChangeResponse
import com.software.hemis.data.main.profile.ProfileResponse
import com.software.hemis.database.room.AttendanceDao
import com.software.hemis.database.room.ProfileDao
import com.software.hemis.database.room.SemesterDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.main.profile.ProfileEntity
import com.software.hemis.domain.main.profile.ProfileUseCase
import com.software.hemis.domain.main.semester.SemesterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: ProfileDao,
    private val semesterDao: SemesterDao,
    private val attendanceDao: AttendanceDao,
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val passwordChangeState = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Init)
    val mPasswordChangeState: StateFlow<ChangePasswordState> get() = passwordChangeState


    fun getProfile(): LiveData<ProfileEntity> {
        return profileDao.getProfile()
    }

    fun getSemester(): LiveData<List<SemesterEntity>> {
        return semesterDao.getSemester()
    }


    fun changePassword(passwordChangeRequest: PasswordChangeRequest) {
        viewModelScope.async {
            profileUseCase.changePassword(passwordChangeRequest)
                .onStart {
                    passwordChangeState.value = ChangePasswordState.IsLoading(true)
                }.catch { exception ->
                    passwordChangeState.value = ChangePasswordState.IsLoading(false)
                    passwordChangeState.value = ChangePasswordState.ShowToast(exception.message.toString())
                }.collect { result ->
                    passwordChangeState.value = ChangePasswordState.IsLoading(false)
                    when (result) {
                        is BaseResult.Success ->
                            passwordChangeState.value =
                                ChangePasswordState.SuccessPassword(result.data.message?:"")
                        is BaseResult.Error -> passwordChangeState.value =
                            ChangePasswordState.ErrorPassword(result.rawResponse)
                    }
                }
        }.onAwait
    }

    fun attendanceAll(){

    }
}

sealed class ProfileState {
    object Init : ProfileState()
    data class IsLoading(val isLoading: Boolean) : ProfileState()
    data class ShowToast(val message: String) : ProfileState()
    data class SuccessProfile(val profileEntity: ProfileEntity) : ProfileState()
    data class ErrorProfile(val rawResponse: WrappedResponse<ProfileResponse>) : ProfileState()
}

sealed class ChangePasswordState {
    object Init : ChangePasswordState()
    data class IsLoading(val isLoading: Boolean) : ChangePasswordState()
    data class ShowToast(val message: String) : ChangePasswordState()
    data class SuccessPassword(val message: String) : ChangePasswordState()
    data class ErrorPassword(val rawResponse: WrappedResponse<PasswordChangeResponse>) : ChangePasswordState()
}