package com.software.hemis.data.main.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("student_id_number")
	val studentIdNumber: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("level")
	val level: Level,

	@field:SerializedName("birth_date")
	val birthDate: Int,

	@field:SerializedName("second_name")
	val secondName: String,

	@field:SerializedName("semester")
	val semester: Semester,

	@field:SerializedName("third_name")
	val thirdName: String,

	@field:SerializedName("first_name")
	val firstName: String,

	@field:SerializedName("group")
	val group: Group
)

data class Level(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("name")
	val name: String
)

data class Group(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class Semester(

	@field:SerializedName("current")
	val current: Boolean,

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("education_year")
	val educationYear: EducationYear,

	@field:SerializedName("id")
	val id: Int
)

data class EducationYear(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("current_status")
	val current: Int
)
