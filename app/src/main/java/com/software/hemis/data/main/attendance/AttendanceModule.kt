package com.software.hemis.data.main.attendance

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.AttendanceDao
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.domain.main.attendance.AttendanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class AttendanceModule {

    @Singleton
    @Provides
    fun provideAttendanceApi(retrofit: Retrofit): AttendanceApi {
        return retrofit.create(AttendanceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAttendanceDao(roomDatabase: RoomDatabase): AttendanceDao {
        return roomDatabase.attendanceDao
    }

    @Singleton
    @Provides
    fun provideAttendanceRepository(
        attendanceApi: AttendanceApi,
        attendanceDao: AttendanceDao
    ): AttendanceRepository {
        return AttendanceRepositoryImp(attendanceApi, attendanceDao)
    }

}