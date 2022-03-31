package com.software.hemis.data.main.resources

import com.google.gson.annotations.SerializedName

data class ResourceResponse(

	@field:SerializedName("trainingType")
	val trainingType: TrainingType? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("subjectTopic")
	val subjectTopic: SubjectTopic? = null,

	@field:SerializedName("files")
	val files: List<FilesItem?>? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null
)

data class TrainingType(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class SubjectTopic(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class FilesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Employee(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
