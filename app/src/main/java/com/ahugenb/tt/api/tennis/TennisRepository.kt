package com.ahugenb.tt.api.tennis

import android.util.Log
import com.ahugenb.tt.tournament.Tour
import com.ahugenb.tt.tournament.model.Tournament
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TennisRepository(private val apiService: TennisApiService) {

    fun fetchTournaments(tour: Tour, year: Int, category: Category = Category.NONE): Flow<List<Tournament>> = flow {
        try {
            val result = mutableListOf<Tournament>()
            result.addAll(
                when (tour) {
                    Tour.ATP -> {
                        apiService.getAtpTournamentList(year, category.path).tournaments
                    }

                    Tour.WTA -> {
                        apiService.getWtaTournamentList(year).tournaments
                    }
                }
            )
            emit(result)
        } catch (e: Exception) {
            Log.e("TennisRepository::fetchTournaments", e.toString())
            emit(emptyList())
        }
    }
}