package com.software.hemis.data.main.taskUpload

import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.utils.data.Constants
import okhttp3.MultipartBody
import org.w3c.dom.Comment
import retrofit2.Response
import retrofit2.http.*

interface TaskDetailsApi {

    @GET
    suspend fun taskDetails(
        @Url string: String
    ): Response<WrappedResponse<TaskDetailsResponse>>

    @Multipart
    @POST
    suspend fun taskUpload(
        @Url url: String,
        @Part files: List<MultipartBody.Part>,
    ):Response<WrappedResponse<TaskUploadResponse>>
}