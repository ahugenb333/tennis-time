package com.ahugenb.tt.api

import android.util.Log
import com.ahugenb.tt.match.domain.Match
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow


class TennisRepository(private val apiService: TennisApiService) {

    fun getMatches(): Flow<List<Match>> = flow {
        try {
            val matches = apiService.getMatches()
            if (matches.isNotEmpty()) {
                emit(ApiResponse.Success(matches))
            } else {
                emit(ApiResponse.None)
            }
        } catch (e: Exception) {
            Log.e("TennisRepository::getMatches", e.toString())
            emit(ApiResponse.Error(e))
        }
    }
}