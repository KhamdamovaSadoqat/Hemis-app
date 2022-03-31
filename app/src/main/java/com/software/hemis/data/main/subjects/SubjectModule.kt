package com.software.hemis.data.main.subjects

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.database.room.SubjectDao
import com.software.hemis.domain.main.subject.SubjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class SubjectModule {

    @Singleton
    @Provides
    fun provideSubjectApi(retrofit: Retrofit): SubjectApi {
        return retrofit.create(SubjectApi::class.java)
    }

    @Provides
    fun provideSubjectDao(roomDatabase: RoomDatabase): SubjectDao {
        return roomDatabase.subjectDao
    }

    @Singleton
    @Provides
    fun provideSubjectRepository(
        subjectApi: SubjectApi,
        subjectDao: SubjectDao
    ): SubjectRepository {
        return SubjectRepositoryImp(subjectApi, subjectDao)
    }
}