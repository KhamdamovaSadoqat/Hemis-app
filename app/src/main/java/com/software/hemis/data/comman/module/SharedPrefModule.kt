package com.software.hemis.data.comman.module

import android.content.Context
import com.software.hemis.utils.data.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) : SharedPref {
        return SharedPref(context)
    }
}