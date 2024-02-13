package com.ahugenb.tt.api

import android.util.Log
import com.ahugenb.tt.match.domain.Match
import com.ahugenb.tt.match.response.toDomainMatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class TennisRepository(private val apiService: TennisApiService) {

    fun getMatches(): Flow<List<Match>> = flow {
        val response = apiService.getMatches()
        when (response) {
            is ApiResponse.Success -> {
                emit(response.data.matchResponse.map { it.toDomainMatch() })
            }
            is ApiResponse.Error -> {
                //todo show error to user
                emit(emptyList())
            }
            ApiResponse.None -> emit(emptyList())
        }
    }.catch { e ->
        Log.e("TennisRepository::getMatches", e.toString())
        emit(emptyList())
    }
}