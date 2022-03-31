package com.software.hemis.data.auth.university

import com.software.hemis.data.comman.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET

interface UniversityApi {

    @GET("public/university-api-urls")
    suspend fun university(): Response<WrappedListResponse<UniversityResponse>>
}