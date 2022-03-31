package com.software.hemis.data.main.attendance

import com.software.hemis.data.comman.WrappedListResponse
import com.software.hemis.utils.data.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface AttendanceApi {

    @GET
    suspend fun resource(
        @Url string: String = "${Constants.universityUrl}education/attendance"
    ): Response<WrappedListResponse<AttendanceResponse>>
}