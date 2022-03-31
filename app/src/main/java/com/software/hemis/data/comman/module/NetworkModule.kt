package com.software.hemis.data.comman.module

import com.software.hemis.data.comman.RequestInterceptor
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(okHttp)
            baseUrl(Constants.baseUrl)
        }.build()
    }


    @Singleton
    @Provides
    fun provideOkHttp(
        requestInterceptor: RequestInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            addInterceptor(httpLoggingInterceptor)
        }
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            okHttpClient.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            okHttpClient.hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return okHttpClient.build()

    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    fun provideRequestInterceptor(prefs: SharedPref): RequestInterceptor {
        return RequestInterceptor(prefs)
    }

}