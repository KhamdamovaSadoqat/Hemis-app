package com.software.hemis.data.main.subjectDetails

import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.subjects.SubjectResponse
import com.software.hemis.utils.data.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface SubjectDetailsApi {

    @GET
    suspend fun subjectDetails(
        @Url string: String,
    ): Response<WrappedResponse<SubjectDetailsResponse>>

}