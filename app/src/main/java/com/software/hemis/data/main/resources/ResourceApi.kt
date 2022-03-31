package com.software.hemis.data.main.resources

import com.software.hemis.data.comman.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ResourceApi {

    @GET
    suspend fun resource(
        @Url string: String,
    ): Response<WrappedListResponse<ResourceResponse>>
}