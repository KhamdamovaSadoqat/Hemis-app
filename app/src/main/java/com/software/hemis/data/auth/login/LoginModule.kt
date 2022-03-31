package com.software.hemis.data.auth.login

import com.software.hemis.data.comman.module.NetworkModule
import com.software.hemis.domain.auth.login.LoginRepository
import com.software.hemis.utils.data.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi, pref: SharedPref): LoginRepository {
        return LoginRepositoryImp(pref, loginApi)
    }
}