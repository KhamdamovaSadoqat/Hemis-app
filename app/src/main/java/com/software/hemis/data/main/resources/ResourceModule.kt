package com.software.hemis.data.main.resources

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.ResourceDao
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.domain.main.resources.ResourceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ResourceModule {

    @Singleton
    @Provides
    fun provideResourceApi(retrofit: Retrofit): ResourceApi {
        return retrofit.create(ResourceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideResourceDao(roomDatabase: RoomDatabase): ResourceDao {
        return roomDatabase.resourceDao
    }

    @Singleton
    @Provides
    fun provideResourceRepository(resourceApi: ResourceApi, resourceDao: ResourceDao): ResourceRepository {
        return ResourceRepositoryImp(resourceApi, resourceDao)
    }
}