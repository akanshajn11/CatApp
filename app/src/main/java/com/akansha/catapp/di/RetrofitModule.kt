package com.akansha.catapp.di

import com.akansha.catapp.BuildConfig
import com.akansha.catapp.data.source.network.api.HeaderInterceptor
import com.akansha.catapp.data.source.network.api.RetrofitNetworkApi
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesMoshiSerializer(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideAuthHttpClient(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: Lazy<OkHttpClient>,
        moshi: Moshi
    ): RetrofitNetworkApi {
        return Retrofit.Builder()
            .client(okHttpClient.get())
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RetrofitNetworkApi::class.java)
    }
}