package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.schedule.ScheduleEntity

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Query("Select * from `ScheduleEntity` where :day = lessonDate  order by  lessonDate ")
    fun getSchedule(day: Int): LiveData<List<ScheduleEntity>>

    @Query("DELETE from `ScheduleEntity`")
    fun deleteSchedule()

}