package com.ahugenb.tt.api.matches.modules

import com.ahugenb.tt.api.matches.MatchesApiService
import com.ahugenb.tt.api.matches.MatchesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MatchesRepositoryModule {

    @Provides
    @Singleton
    fun provideMatchesRepository(matchesApiService: MatchesApiService): MatchesRepository =
        MatchesRepository(matchesApiService)
}  