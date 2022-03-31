package com.software.hemis.data.main.semester

import com.google.gson.annotations.SerializedName

data class SemesterResponse(

	@field:SerializedName("current")
	val current: Boolean? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("weeks")
	val weeks: List<WeeksItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("education_year")
	val educationYear: EducationYear? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class WeeksItem(

	@field:SerializedName("_semester")
	val semester: String? = null,

	@field:SerializedName("end_date")
	val endDate: Int? = null,

	@field:SerializedName("current")
	val current: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("start_date")
	val startDate: Int? = null
)

data class EducationYear(

	@field:SerializedName("current")
	val current: Boolean? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

