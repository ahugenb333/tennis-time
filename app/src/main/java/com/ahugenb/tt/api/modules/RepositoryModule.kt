package com.ahugenb.tt.api.modules

import com.ahugenb.tt.api.TennisApiService
import com.ahugenb.tt.api.TennisRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTennisRepository(apiService: TennisApiService): TennisRepository =
        TennisRepository(apiService)
}