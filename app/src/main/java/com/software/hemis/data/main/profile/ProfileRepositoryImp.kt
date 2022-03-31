package com.software.hemis.data.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.ProfileDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.main.profile.ProfileEntity
import com.software.hemis.domain.main.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImp @Inject
constructor(
    private val profileApi: ProfileApi,
    private val profileDao: ProfileDao,
) : ProfileRepository {
    override suspend fun profile(): Flow<BaseResult<ProfileEntity, WrappedResponse<ProfileResponse>>> {
        return flow {
            val response = profileApi.profile()
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    Log.d("-------------", "response: ${response.body()}")
                    val profileEntity = ProfileEntity(
                        body.data?.image ?: "",
                        body.data?.studentIdNumber ?: "",
                        body.data?.phone ?: "",
                        body.data?.birthDate ?: 0,
                        body.data?.secondName ?: "",
                        body.data?.thirdName ?: "",
                        body.data?.firstName ?: "",
                        body.data?.level?.code ?: "",
                        body.data?.level?.name ?: "",
                        body.data?.group?.name ?: "",
                        body.data?.group?.id ?: -1,
                        body.data?.semester?.current ?: false,
                        body.data?.semester?.code ?: "",
                        body.data?.semester?.name ?: "",
                        body.data?.semester?.id ?: -1,
                        body.data?.semester?.educationYear?.name ?: "",
                        body.data?.semester?.educationYear?.code ?: "",
                        body.data?.semester?.educationYear?.current ?: -1
                    )
                    Log.d("-------------", "profile: ${profileEntity.toString()}")

                    emit(BaseResult.Success(profileEntity))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<ProfileResponse>>() {}.type
                val err: WrappedResponse<ProfileResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun passwordChange(passwordChangeRequest: PasswordChangeRequest): Flow<BaseResult<PasswordChangeResponse, WrappedResponse<PasswordChangeResponse>>> {
        return flow {
            val response = profileApi.passwordChange(passwordChangeRequest)
            if (response.isSuccessful && response.body() != null) {
                emit(BaseResult.Success(response.body()!!.data!!))
            }
        }
    }

    override suspend fun insertProfile(profileEntity: ProfileEntity) {
        profileDao.insertProfile(profileEntity)
    }

    override suspend fun getProfile(): LiveData<ProfileEntity> {
        return profileDao.getProfile()
    }

}