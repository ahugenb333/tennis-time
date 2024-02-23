package com.ahugenb.tt.api.tournaments

import android.util.Log
import com.ahugenb.tt.tournament.model.Tournament
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TennisRepository(private val apiService: TennisApiService) {

    fun fetchTournaments(tour: Tour, year: Int, category: Category = Category.NONE): Flow<List<Tournament>> = flow {
        try {
            val result = apiService.getTournamentList(tour.path, year, category.path)
            emit(result.tournaments)
        } catch (e: Exception) {
            Log.e("TennisRepository::fetchTournaments", e.toString())
            emit(emptyList())
        }
    }
}