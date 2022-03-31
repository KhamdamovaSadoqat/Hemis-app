package com.software.hemis.domain.main.profile

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.profile.PasswordChangeRequest
import com.software.hemis.data.main.profile.PasswordChangeResponse
import com.software.hemis.data.main.profile.ProfileResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun profile():
            Flow<BaseResult<ProfileEntity, WrappedResponse<ProfileResponse>>>

    suspend fun passwordChange(passwordChangeRequest: PasswordChangeRequest):
            Flow<BaseResult<PasswordChangeResponse, WrappedResponse<PasswordChangeResponse>>>

    suspend fun insertProfile(profileEntity: ProfileEntity)

    suspend fun getProfile(): LiveData<ProfileEntity>
}