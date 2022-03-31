package com.software.hemis.data.main.schedule

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(

	@field:SerializedName("lessonPair")
	val lessonPair: LessonPair? = null,

	@field:SerializedName("_week")
	val week: Int? = null,

	@field:SerializedName("auditorium")
	val auditorium: Auditorium? = null,

	@field:SerializedName("subject")
	val subject: Subject? = null,

	@field:SerializedName("weekEndTime")
	val weekEndTime: Int? = null,

	@field:SerializedName("additional")
	val additional: String? = null,

	@field:SerializedName("weekStartTime")
	val weekStartTime: Int? = null,

	@field:SerializedName("trainingType")
	val trainingType: TrainingType? = null,

	@field:SerializedName("lesson_date")
	val lessonDate: Int? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null
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

data class Subject(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Auditorium(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class TrainingType(

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
