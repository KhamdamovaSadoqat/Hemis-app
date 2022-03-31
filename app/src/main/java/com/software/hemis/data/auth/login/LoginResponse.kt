package com.software.hemis.data.auth.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("token")
	val token: String? = null
)

