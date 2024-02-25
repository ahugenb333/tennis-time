package com.ahugenb.tt.api.tennis

import android.util.Log
import com.ahugenb.tt.tournament.model.AtpTournament
import com.ahugenb.tt.tournament.model.WtaTournament
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TennisRepository(private val apiService: TennisApiService) {

    fun fetchAtpTournaments(year: Int, category: Category): Flow<List<AtpTournament>> = flow {
        try {
            val result = apiService.getAtpTournamentList(year, category.path).tournaments

            emit(result)
        } catch (e: Exception) {
            Log.e("TennisRepository::fetchAtpTournaments", e.toString())
            emit(emptyList())
        }
    }

    fun fetchWtaTournaments(year: Int): Flow<List<WtaTournament>> = flow {
        try {
            val result = apiService.getWtaTournamentList(year).tournaments

            emit(result)
        } catch (e: Exception) {
            Log.e("TennisRepository::fetchAtpTournaments", e.toString())
            emit(emptyList())
        }
    }
}