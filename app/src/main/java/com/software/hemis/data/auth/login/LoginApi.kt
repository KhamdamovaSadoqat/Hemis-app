package com.software.hemis.data.auth.login

import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.utils.data.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginApi {

    @POST
    suspend fun login(
        @Url
        url: String = "${Constants.universityUrl}auth/login",
        @Body login: LoginRequest,
    ): Response<WrappedResponse<LoginResponse>>

    @POST
    suspend fun refresh(
        @Url
        url: String = "${Constants.universityUrl}auth/refresh-token",
        @Header("Cookie") cookie: String,
    ): Response<WrappedResponse<LoginResponse>>
}
