package com.software.hemis.data.main.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(

	@field:SerializedName("trainingType")
	val trainingType: TrainingType? = null,

	@field:SerializedName("subject")
	val subject: Subject? = null,

	@field:SerializedName("lesson_date")
	val lessonDate: Int? = null,

	@field:SerializedName("semester")
	val semester: Semester? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("absent_on")
	val absentOn: Int? = null,

	@field:SerializedName("absent_off")
	val absentOff: Int? = null,

	@field:SerializedName("lessonPair")
	val lessonPair: LessonPair? = null
)

data class TrainingType(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class EducationYear(

	@field:SerializedName("current")
	val current: Boolean? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class Employee(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Subject(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class LessonPair(

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("end_time")
	val endTime: String? = null
)

data class Semester(

	@field:SerializedName("current")
	val current: Boolean? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("education_year")
	val educationYear: EducationYear? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
