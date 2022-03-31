package com.software.hemis.data.auth.login

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.domain.auth.login.LoginRepository
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.utils.data.SharedPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImp @Inject
constructor(
    private var shared: SharedPref,
    private val loginApi: LoginApi
) : LoginRepository {
    override suspend fun login(
        url: String,
        loginRequest: LoginRequest
    ): Flow<BaseResult<Unit, WrappedResponse<LoginResponse>>> {
        val response = loginApi.login(login = loginRequest)
        return flow {
            if (response.isSuccessful) {
                shared.token = response.body()!!.data?.token
                shared.refreshToken = response.headers()["Set-Cookie"]
                emit(BaseResult.Success(Unit))
            } else {
                val type = object : TypeToken<WrappedResponse<LoginResponse>>() {}.type
                val err: WrappedResponse<LoginResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun refresh(): Flow<BaseResult<Unit, WrappedResponse<String>>> {
        return flow {
            val response = loginApi.refresh(
                cookie = shared.refreshToken!!
            )
            if (response.body()!!.success && response.body() != null) {
                shared.token = response.body()!!.data?.token
                shared.refreshToken = response.headers()["Set-Cookie"]
                emit(BaseResult.Success(Unit))
            } else {
                val type = object : TypeToken<WrappedResponse<String>>() {}.type
                val err: WrappedResponse<String> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }


}