package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.subject.SubjectWithSubjectDetails

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Query("Select * from `SubjectEntity` where :semesterCode = semesterCode")
    fun getSubjectList(semesterCode: Int): LiveData<List<SubjectEntity>>

    @Query("Select * from `SubjectEntity` where :subjectId = subjectId")
    fun getSubject(subjectId: Int): LiveData<SubjectEntity>

    @Transaction
    @Query("Select * from `SubjectEntity` where :semesterCode = semesterCode")
    fun getSubjectWithSubjectDetails(semesterCode: Int): LiveData<List<SubjectWithSubjectDetails>>

}