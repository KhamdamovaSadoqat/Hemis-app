package com.software.hemis.data.main.schedule

import com.software.hemis.data.comman.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ScheduleApi {

    @GET
    suspend fun schedule(
        @Url string: String,
    ): Response<WrappedListResponse<ScheduleResponse>>

}