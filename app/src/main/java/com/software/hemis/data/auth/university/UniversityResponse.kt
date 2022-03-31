package com.software.hemis.data.auth.university

import com.google.gson.annotations.SerializedName

data class UniversityResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("api_url")
	val apiUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

