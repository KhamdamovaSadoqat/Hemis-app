package com.software.hemis.data.main.profile

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.ProfileDao
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.domain.main.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ProfileModule {

    @Singleton
    @Provides
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileDao(roomDatabase: RoomDatabase): ProfileDao{
        return roomDatabase.profileDao
    }

    @Singleton
    @Provides
    fun provideProfileRepository(profileApi: ProfileApi, profileDao: ProfileDao): ProfileRepository {
        return ProfileRepositoryImp(profileApi, profileDao)
    }
}
