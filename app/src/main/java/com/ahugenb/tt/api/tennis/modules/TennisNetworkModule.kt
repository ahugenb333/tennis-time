package com.ahugenb.tt.api.tennis.modules

import com.ahugenb.tt.api.tennis.TennisApiService
import com.ahugenb.tt.api.tennis.TennisHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TennisNetworkModule {

    @Provides
    @Singleton
    @Named("TennisOkHttpClient")
    fun provideTennisOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(TennisHeaderInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("TennisRetrofit")
    fun provideTennisRetrofit(@Named("TennisOkHttpClient") tennisOkHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://ultimate-tennis1.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(tennisOkHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideTennisApiService(@Named("TennisRetrofit") tennisRetrofit: Retrofit): TennisApiService =
        tennisRetrofit.create(TennisApiService::class.java)
}