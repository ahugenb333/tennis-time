package com.ahugenb.tt.api.matches.modules

import com.ahugenb.tt.api.matches.MatchesApiService
import com.ahugenb.tt.api.matches.MatchesHeaderInterceptor
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
object MatchesNetworkModule {

    @Provides
    @Singleton
    @Named("MatchesOkHttpClient")
    fun provideMatchesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(MatchesHeaderInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("MatchesRetrofit")
    fun provideMatchesRetrofit(@Named("MatchesOkHttpClient") matchesOkHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://zylalabs.com/api/961/live+tennis+api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(matchesOkHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideMatchesApiService(@Named("MatchesRetrofit") matchesRetrofit: Retrofit): MatchesApiService =
        matchesRetrofit.create(MatchesApiService::class.java)
}