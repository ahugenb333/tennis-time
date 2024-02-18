package com.ahugenb.tt.match

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.tt.api.TennisRepository
import com.ahugenb.tt.match.detail.response.Statistic
import com.ahugenb.tt.match.list.model.domain.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MatchListUIState {
    data object Empty : MatchListUIState()

    data object Loading : MatchListUIState()
    data class All(val matches: List<Match>) : MatchListUIState()
}

sealed class MatchDetailUIState {

    data object Empty : MatchDetailUIState()

    data object Loading : MatchDetailUIState()

    data object Populated : MatchDetailUIState()
}


@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: TennisRepository
) : ViewModel() {

    private val _matchListUIState = MutableStateFlow<MatchListUIState>(MatchListUIState.Empty)
    val matchListUIState: StateFlow<MatchListUIState> = _matchListUIState

    private val _matchDetailUIState =
        MutableStateFlow<MatchDetailUIState>(MatchDetailUIState.Empty)
    val matchDetailUIState: StateFlow<MatchDetailUIState> = _matchDetailUIState

    init {
        fetchMatches()
    }

    fun fetchMatches() {
        _matchListUIState.value = MatchListUIState.Loading
        viewModelScope.launch {
            repository.fetchMatches()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("MatchesViewModel::fetchMatches", e.toString())
                    emit(emptyList())
                }
                .collect { matches ->
                    _matchListUIState.value = if (matches.isNotEmpty()) {
                        MatchListUIState.All(matches)
                    } else {
                        MatchListUIState.Empty
                    }
                }
        }
    }

    fun fetchMatchDetails(matchId: String) {
        _matchDetailUIState.value = MatchDetailUIState.Loading
        viewModelScope.launch {
            repository.fetchMatchDetails(matchId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("MatchesViewModel::fetchMatchDetails", e.toString())
                    emit(emptyList())
                }
                .collect { statistics ->
                    if (statistics.isNotEmpty()) {
                        _matchDetailUIState.value = MatchDetailUIState.Populated
                        updateMatchWithStatistic(statistics[0], matchId)
                    } else {
                        MatchDetailUIState.Empty
                    }
                }
        }
    }

    private fun updateMatchWithStatistic(statistic: Statistic, matchId: String) {
        if (_matchListUIState.value is MatchListUIState.All) {
            val updatedMatches =
                (_matchListUIState.value as MatchListUIState.All).matches.map { match ->
                    if (match.id == matchId) {
                        match.copy(statistic = statistic)
                    } else {
                        match
                    }
                }
            _matchListUIState.value = MatchListUIState.All(updatedMatches)
        }
    }
}