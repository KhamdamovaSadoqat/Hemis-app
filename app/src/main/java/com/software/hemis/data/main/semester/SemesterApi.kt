package com.software.hemis.data.main.semester

import com.software.hemis.data.comman.WrappedListResponse
import com.software.hemis.utils.data.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SemesterApi {

    @GET
    suspend fun semester(
        @Url string: String = "${Constants.universityUrl}education/semesters"
    ): Response<WrappedListResponse<SemesterResponse>>


}