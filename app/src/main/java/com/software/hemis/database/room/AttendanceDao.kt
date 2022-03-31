package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.attendance.AttendanceEntity

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("Select * from `AttendanceEntity` where :subjectId = subjectId and :semesterId = semesterId")
    fun getAttendanceBySubject(subjectId: Int, semesterId: Int): LiveData<List<AttendanceEntity>>


}