package com.ahugenb.tt.api.matches

import android.util.Log
import com.ahugenb.tt.match.list.model.MatchListUtils.Companion.toDomainMatch
import com.ahugenb.tt.match.list.model.domain.Match
import com.ahugenb.tt.match.list.model.response.Statistic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MatchesRepository(private val apiService: MatchesApiService) {

    fun fetchMatches(): Flow<List<Match>> = flow {
        try {
            val response = apiService.getLiveScores().matchResponse.map {
                it.toDomainMatch()
            }
            emit(response)
        } catch(e: Exception) {
            Log.e("MatchesRepository::fetchMatches", e.toString())
            emit(emptyList())
        }
    }

    fun fetchMatchDetails(matchId: String): Flow<List<Statistic>> = flow {
        try {
            val response = apiService.getMatchDetails(matchId).statistics
            emit(response)
        } catch(e: Exception) {
            Log.e("MatchesRepository::fetchMatchDetails", e.toString())
            emit(emptyList())
        }
    }
}