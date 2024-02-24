package com.ahugenb.tt.tournament

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.tt.api.tennis.Category
import com.ahugenb.tt.api.tennis.TennisRepository
import com.ahugenb.tt.tournament.model.Tournament
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Tour {
    ATP,
    WTA
}

sealed class TournamentListUIState {

    data object Empty : TournamentListUIState()

    data object Loading : TournamentListUIState()

    data class All(val tournaments: List<Tournament>) : TournamentListUIState()
}
@HiltViewModel
class TournamentViewModel @Inject constructor(
    private val repository: TennisRepository
) : ViewModel() {

    private val _tournamentListUIState = MutableStateFlow<TournamentListUIState>(TournamentListUIState.Empty)
    val tournamentListUIState: StateFlow<TournamentListUIState> = _tournamentListUIState

    init {
        fetchTournaments()
    }

    private fun fetchTournaments(
        tour: Tour = Tour.ATP,
        year: Int = 2024,
        category: Category = Category.ATPGS
    ) {
        _tournamentListUIState.value = TournamentListUIState.Loading
        viewModelScope.launch {
            repository.fetchTournaments(tour, year, category)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("TournamentViewModel::fetchTournaments", e.toString())
                    emit(emptyList())
                }
                .collect { result ->
                    _tournamentListUIState.value = TournamentListUIState.All(result)
                }
        }
    }

}