package com.software.hemis.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.task.TaskDetailEntity
import com.software.hemis.domain.main.task.TaskSentSubmissionEntity

@Dao
interface TaskDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskDetails(taskDetailEntity: TaskDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskSentSubmission(taskSentSubmissionEntity: TaskSentSubmissionEntity)

    @Query("Update TaskEntity set taskStatus = :taskStatus, taskStatusCode = :taskStatusCode where id =:taskId")
    suspend fun updateTask(taskId: Int, taskStatus: String, taskStatusCode: Int)


}