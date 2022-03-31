package com.software.hemis.data.main.taskUpload

import com.google.gson.annotations.SerializedName

data class TaskDetailsResponse(

	@field:SerializedName("trainingType")
	val trainingType: TrainingType? = null,

	@field:SerializedName("attempt_count")
	val attemptCount: Int? = null,

	@field:SerializedName("studentTaskActivity")
	val studentTaskActivity: StudentTaskActivity? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("studentTaskActivities")
	val studentTaskActivities: List<StudentTaskActivitiesItem?>? = null,

	@field:SerializedName("max_ball")
	val maxBall: Int? = null,

	@field:SerializedName("attempt_limit")
	val attemptLimit: Int? = null,

	@field:SerializedName("taskType")
	val taskType: TaskType? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Int? = null,

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

data class FilesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
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

data class StudentTaskActivity(

	@field:SerializedName("_task")
	val task: Int? = null,

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

data class Employee(

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

data class StudentTaskActivitiesItem(

	@field:SerializedName("_task")
	val task: Int? = null,

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
