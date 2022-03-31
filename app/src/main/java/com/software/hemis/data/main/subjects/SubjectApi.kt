package com.software.hemis.data.main.subjects

import com.software.hemis.data.comman.WrappedListResponse
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.utils.data.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SubjectApi {

    @GET
    suspend fun subjects(
        @Url string: String = "${Constants.universityUrl}education/subjects"
    ): Response<WrappedListResponse<SubjectResponse>>

}