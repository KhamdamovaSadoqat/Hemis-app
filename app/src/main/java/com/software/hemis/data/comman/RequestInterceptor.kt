package com.software.hemis.data.comman

import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SharedPref) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "Bearer ${pref.token}"
        val newRequest = chain.request().newBuilder()
        if (token.isNotEmpty()) {
            newRequest
                .addHeader("Authorization", token)
        }
        return chain.proceed(newRequest.build())
    }
}