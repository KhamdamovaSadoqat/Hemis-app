package com.software.hemis.data.main.taskUpload

import com.google.gson.annotations.SerializedName

data class TaskUploadResponse(

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
	val markedComment: Any? = null,

	@field:SerializedName("mark")
	val mark: Int? = null,

	@field:SerializedName("marked_date")
	val markedDate: Any? = null
)