package com.software.hemis.data.main.profile

import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.utils.data.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ProfileApi {

    @GET
    suspend fun profile(
        @Url string: String = "${Constants.universityUrl}account/me"

    ): Response<WrappedResponse<ProfileResponse>>

    @POST
    suspend fun passwordChange(
        @Body passwordChangeRequest: PasswordChangeRequest,
        @Url string: String = "${Constants.universityUrl}account/update"
    ): Response<WrappedResponse<PasswordChangeResponse>>

}