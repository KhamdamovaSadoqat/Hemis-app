package com.software.hemis.data.auth.university

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.domain.auth.university.UniversityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UniversityModule {

    @Singleton
    @Provides
    fun provideUniversityApi(retrofit: Retrofit): UniversityApi {
        return retrofit.create(UniversityApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUniversityRepository(universityApi: UniversityApi): UniversityRepository {
        return UniversityRepositoryImp( universityApi)
    }
}