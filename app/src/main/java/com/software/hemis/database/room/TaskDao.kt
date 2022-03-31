package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.software.hemis.domain.main.task.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Transaction
    @Query("Select * from `TaskEntity` where :subjectId = subjectId and :taskStatusCode = taskStatusCode and :semesterId = semesterId order by deadline")
    fun getTask(subjectId: Int, taskStatusCode: Int, semesterId: Int): LiveData<List<TaskWithSubject>>

    @Transaction
    @Query("Select * from `TaskEntity` where :subjectId = subjectId and :semesterId = semesterId order by deadline")
    fun getAllTaskBySubject(subjectId: Int, semesterId: Int): LiveData<List<TaskWithSubject>>

    @Transaction
    @Query("Select * from `TaskEntity` where :taskStatusCode = taskStatusCode and :semesterId = semesterId order by deadline")
    fun getAllDeadlineTask(taskStatusCode: Int, semesterId: Int): LiveData<List<TaskWithSubject>>

    @Transaction
    @Query("Select * from `TaskEntity` where :semesterId = semesterId order by deadline")
    fun getAllTask(semesterId: Int): LiveData<List<TaskWithSubject>>

    @Transaction
    @Query("Select * from `TaskEntity` where :subjectId = subjectId and :semesterId = semesterId and :taskId = id")
    fun getTaskWithDetails(subjectId: Int, semesterId: Int, taskId: Int): LiveData<TaskWithTaskDetail>

    @Transaction
    @Query("Select * from `TaskSentSubmissionEntity` where :taskId = id")
    fun getTaskSentSubmission(taskId: Int): LiveData<TaskSentSubmissionEntity>


}