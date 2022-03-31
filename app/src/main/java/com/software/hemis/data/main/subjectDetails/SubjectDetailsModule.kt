package com.software.hemis.data.main.subjectDetails

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.database.room.SubjectDetailsDao
import com.software.hemis.database.room.TaskDao
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class SubjectDetailsModule {

    @Singleton
    @Provides
    fun provideSubjectDetailsApi(retrofit: Retrofit): SubjectDetailsApi {
        return retrofit.create(SubjectDetailsApi::class.java)
    }

    @Provides
    fun provideSubjectDetailsDao(roomDatabase: RoomDatabase): SubjectDetailsDao {
        return roomDatabase.subjectDetailsDao
    }

    @Provides
    fun provideTaskDao(roomDatabase: RoomDatabase): TaskDao {
        return roomDatabase.taskDao
    }

    @Singleton
    @Provides
    fun provideSubjectDetailsRepository(
        subjectDetailsApi: SubjectDetailsApi,
        subjectDetailsDao: SubjectDetailsDao,
        taskDao: TaskDao,
    ): SubjectDetailsRepository {
        return SubjectDetailsRepositoryImp(subjectDetailsApi, subjectDetailsDao, taskDao)
    }
}