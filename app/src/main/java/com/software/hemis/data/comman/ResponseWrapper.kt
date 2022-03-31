package com.software.hemis.data.comman

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    @SerializedName("success") var success: Boolean,
    @SerializedName("error") var errors: String? = null,
    @SerializedName("data") var data: List<T>? = null,
    var code: Int
)

data class WrappedResponse<T>(
    @SerializedName("success") var success: Boolean,
    @SerializedName("error") var error: String? = null,
    @SerializedName("data") var data: T? = null,
    var code: Int
)
