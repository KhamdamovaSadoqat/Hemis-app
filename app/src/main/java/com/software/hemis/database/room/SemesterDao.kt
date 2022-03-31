package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.semester.SemesterEntity

@Dao
interface SemesterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSemester(semester: SemesterEntity)

    @Query("Select * from `SemesterEntity`")
    fun getSemester(): LiveData<List<SemesterEntity>>

    @Query("Select * from `SemesterEntity` where current = :currentSemester")
    fun getCurrentSemester(currentSemester: Int): LiveData<SemesterEntity>


}