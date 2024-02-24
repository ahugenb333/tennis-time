package com.ahugenb.tt.api.tennis.modules

import com.ahugenb.tt.api.tennis.TennisApiService
import com.ahugenb.tt.api.tennis.TennisRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TennisRepositoryModule {

    @Provides
    @Singleton
    fun provideTennisRepository(tennisApiService: TennisApiService): TennisRepository =
        TennisRepository(tennisApiService)
}  