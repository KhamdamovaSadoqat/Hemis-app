package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsEntity
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsWithSubject

@Dao
interface SubjectDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjectDetails(subjectDetailsEntity: SubjectDetailsEntity)

    @Query("Select * from `SubjectDetailsEntity` where :subjectId = id")
    fun getSubjectDetails(subjectId: Int): LiveData<SubjectDetailsEntity>

    @Transaction
    @Query("SELECT * FROM `SubjectDetailsEntity` where :subjectId = id")
    fun getSubjectDetailsWithSubject(subjectId: Int): LiveData<SubjectDetailsWithSubject>
}