package com.software.hemis.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.hemis.domain.main.profile.ProfileEntity

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("Select * from `ProfileEntity`")
    fun getProfile(): LiveData<ProfileEntity>
}