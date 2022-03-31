package com.software.hemis.data.main.subjectDetails

import com.google.gson.annotations.SerializedName

data class SubjectDetailsResponse(

	@field:SerializedName("max_ball")
	val maxBall: Int? = null,

	@field:SerializedName("subject_ball")
	val subjectBall: Int? = null,

	@field:SerializedName("submits_count")
	val submitsCount: Int? = null,

	@field:SerializedName("resources_count")
	val resourcesCount: Int? = null,

	@field:SerializedName("subject")
	val subject: Subject? = null,

	@field:SerializedName("absent_count")
	val absentCount: Int? = null,

	@field:SerializedName("total_acload")
	val totalAcload: Int? = null,

	@field:SerializedName("credit")
	val credit: Int? = null,

	@field:SerializedName("subjectType")
	val subjectType: SubjectType? = null,

	@field:SerializedName("tasks_count")
	val tasksCount: Int? = null,

	@field:SerializedName("tasks")
	val tasks: List<TasksItem?>? = null,

	@field:SerializedName("marked_count")
	val markedCount: Int? = null
)

data class StudentTaskActivity(

	@field:SerializedName("send_date")
	val sendDate: Int? = null,

	@field:SerializedName("files")
	val files: List<FilesItem?>? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("marked_comment")
	val markedComment: String? = null,

	@field:SerializedName("mark")
	val mark: Int? = null,

	@field:SerializedName("marked_date")
	val markedDate: Int? = null
)

data class TasksItem(

	@field:SerializedName("trainingType")
	val trainingType: TrainingType? = null,

	@field:SerializedName("attempt_count")
	val attemptCount: Int? = null,

	@field:SerializedName("studentTaskActivity")
	val studentTaskActivity: StudentTaskActivity? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("max_ball")
	val maxBall: Int? = null,

	@field:SerializedName("attempt_limit")
	val attemptLimit: Int? = null,

	@field:SerializedName("taskType")
	val taskType: TaskType? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("files")
	val files: List<FilesItem?>? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deadline")
	val deadline: Int? = null,

	@field:SerializedName("taskStatus")
	val taskStatus: TaskStatus? = null
)

data class TrainingType(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class TaskType(

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

data class TaskStatus(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class FilesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
