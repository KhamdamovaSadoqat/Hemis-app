package com.software.hemis.data.main.schedule

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.database.room.ScheduleDao
import com.software.hemis.domain.main.schedule.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ScheduleModule {

    @Singleton
    @Provides
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi {
        return retrofit.create(ScheduleApi::class.java)
    }

    @Provides
    fun provideScheduleDao(roomDatabase: RoomDatabase): ScheduleDao {
        return roomDatabase.scheduleDao
    }

    @Singleton
    @Provides
    fun provideScheduleRepository(scheduleApi: ScheduleApi, scheduleDao: ScheduleDao): ScheduleRepository {
        return ScheduleRepositoryImp(scheduleApi, scheduleDao)
    }
}
