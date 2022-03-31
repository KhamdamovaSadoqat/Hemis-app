package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.semester.WeekEntity

@Dao
interface WeekDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeek(week: WeekEntity)

    @Query("Select * from `WeekEntity` where :weekId = id")
    fun getWeek(weekId: Int): LiveData<WeekEntity>

}