package com.ahugenb.tt.tournament

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.tt.api.tennis.Category
import com.ahugenb.tt.api.tennis.TennisRepository
import com.ahugenb.tt.tournament.model.AtpTournament
import com.ahugenb.tt.tournament.model.WtaTournament
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

    data class Atp(val tournaments: List<AtpTournament>) : TournamentListUIState()

    data class Wta(val tournaments: List<WtaTournament>) : TournamentListUIState()
}
@HiltViewModel
class TournamentViewModel @Inject constructor(
    private val repository: TennisRepository
) : ViewModel() {

    private val _tournamentListUIState = MutableStateFlow<TournamentListUIState>(TournamentListUIState.Empty)
    val tournamentListUIState: StateFlow<TournamentListUIState> = _tournamentListUIState

    init {
        fetchWtaTournaments()
    }

    private fun fetchAtpTournaments(
        year: Int = 2024,
        category: Category = Category.ATPGS
    ) {
        _tournamentListUIState.value = TournamentListUIState.Loading
        viewModelScope.launch {
            repository.fetchAtpTournaments(year, category)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("TournamentViewModel::fetchTournaments", e.toString())
                    emit(emptyList())
                }
                .collect { result ->
                    _tournamentListUIState.value = TournamentListUIState.Atp(result)
                }
        }
    }

    private fun fetchWtaTournaments(
        year: Int = 2024,
    ) {
        _tournamentListUIState.value = TournamentListUIState.Loading
        viewModelScope.launch {
            repository.fetchWtaTournaments(year)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("TournamentViewModel::fetchWtaTournaments", e.toString())
                    emit(emptyList())
                }
                .collect { result ->
                    _tournamentListUIState.value = TournamentListUIState.Wta(result)
                }
        }
    }

}