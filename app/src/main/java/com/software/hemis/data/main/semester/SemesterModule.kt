package com.software.hemis.data.main.semester

import android.content.Context
import androidx.room.Room
import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.database.room.SemesterDao
import com.software.hemis.database.room.WeekDao
import com.software.hemis.domain.main.semester.SemesterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class SemesterModule {

    @Singleton
    @Provides
    fun provideSemesterApi(retrofit: Retrofit): SemesterApi {
        return retrofit.create(SemesterApi::class.java)
    }

    @Provides
    fun provideSemesterDao(roomDatabase: RoomDatabase): SemesterDao {
        return roomDatabase.semesterDao
    }

    @Provides
    fun provideWeekDao(roomDatabase: RoomDatabase): WeekDao {
        return roomDatabase.weekDao
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): RoomDatabase {
        return Room.databaseBuilder(
            appContext,
            RoomDatabase::class.java,
            "Hemis"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSemesterRepository(
        semesterApi: SemesterApi,
        pref: SharedPref, semesterDao:
        SemesterDao, weekDao: WeekDao
    ): SemesterRepository{
        return SemesterRepositoryImp(semesterApi, pref, semesterDao, weekDao)
    }
}