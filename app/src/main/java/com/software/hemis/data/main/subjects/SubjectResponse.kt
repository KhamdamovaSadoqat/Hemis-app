package com.software.hemis.data.main.subjects

import com.google.gson.annotations.SerializedName

data class SubjectResponse(

	@field:SerializedName("_semester")
	val semester: String? = null,

	@field:SerializedName("subject")
	val subject: Subject? = null,

	@field:SerializedName("total_acload")
	val totalAcload: Int? = null,

	@field:SerializedName("credit")
	val credit: Int? = null,

	@field:SerializedName("subjectType")
	val subjectType: SubjectType? = null
)

data class SubjectType(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class Subject(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
