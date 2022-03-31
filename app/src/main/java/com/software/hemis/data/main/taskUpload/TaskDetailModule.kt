package com.software.hemis.data.main.taskUpload

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.database.room.TaskDao
import com.software.hemis.database.room.TaskDetailDao
import com.software.hemis.domain.main.task.TaskDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class TaskDetailModule {

    @Singleton
    @Provides
    fun provideTaskDetailsApi(retrofit: Retrofit): TaskDetailsApi {
        return retrofit.create(TaskDetailsApi::class.java)
    }


    @Provides
    fun provideTaskDetailsDao(roomDatabase: RoomDatabase): TaskDetailDao {
        return roomDatabase.taskDetailDao
    }

    @Singleton
    @Provides
    fun provideTaskDetailsRepository(
        taskDetailDao: TaskDetailDao,
        taskDetailsApi: TaskDetailsApi,
        taskDao: TaskDao
    ): TaskDetailsRepository {
        return TaskDetailsDetailsRepositoryImp(taskDetailsApi, taskDetailDao, taskDao)
    }
}