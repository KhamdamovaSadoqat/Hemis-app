package com.software.hemis.data.main.profile

import com.google.gson.annotations.SerializedName

data class PasswordChangeRequest(

	@field:SerializedName("change_password")
	val changePassword: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("confirmation")
	val confirmation: String? = null
)
