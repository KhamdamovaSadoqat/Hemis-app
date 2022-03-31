package com.software.hemis.domain.auth.login

import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.auth.login.LoginRequest
import com.software.hemis.data.auth.login.LoginResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(url:String,loginRequest: LoginRequest):
            Flow<BaseResult<Unit, WrappedResponse<LoginResponse>>>

    suspend fun refresh(): Flow<BaseResult<Unit, WrappedResponse<String>>>
}