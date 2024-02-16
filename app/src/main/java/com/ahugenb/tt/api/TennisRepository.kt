package com.ahugenb.tt.api

import android.util.Log
import com.ahugenb.tt.match.list.MatchListUtils.Companion.toDomainMatch
import com.ahugenb.tt.match.list.domain.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TennisRepository(private val apiService: TennisApiService) {

    fun fetchMatches(): Flow<List<Match>> = flow {
        try {
            val response = apiService.getLiveScores().matchResponse.map {
                it.toDomainMatch()
            }
            emit(response)
        } catch(e: Exception ) {
            Log.e("TennisRepository::fetchMatches", e.toString())
            emit(emptyList())
        }
    }
}