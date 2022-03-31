package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.resources.ResourceEntity

@Dao
interface ResourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: ResourceEntity)

    @Query("Select * from `ResourceEntity` where :subjectId = subjectId and :semesterId = semesterId and :typeId = trainingTypeCode ")
    fun getResource(subjectId: Int, semesterId: Int, typeId: Int): LiveData<List<ResourceEntity>>
}