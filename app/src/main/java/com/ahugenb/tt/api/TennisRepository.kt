package com.ahugenb.tt.api

import android.util.Log
import com.ahugenb.tt.match.detail.model.Statistic
import com.ahugenb.tt.match.list.model.MatchListUtils.Companion.toDomainMatch
import com.ahugenb.tt.match.list.model.domain.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TennisRepository(private val apiService: TennisApiService) {

    fun fetchMatches(): Flow<List<Match>> = flow {
        try {
            val response = apiService.getLiveScores().matchResponse.map {
                it.toDomainMatch()
            }
            emit(response)
        } catch(e: Exception) {
            Log.e("TennisRepository::fetchMatches", e.toString())
            emit(emptyList())
        }
    }

    fun fetchMatchDetails(matchId: String): Flow<List<Statistic>> = flow {
        try {
            val response = apiService.getMatchDetails(matchId).statistics
            emit(response)
        } catch(e: Exception) {
            Log.e("TennisRepository::fetchMatchDetails", e.toString())
            emit(emptyList())
        }
    }
}