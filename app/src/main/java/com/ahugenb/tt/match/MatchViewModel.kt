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

    //loadingMatchId is needed or else all the cards display a loading indicator
    data class Loading(val loadingMatchId: String) : MatchDetailUIState()

    data class All(val selectedMatchId: String) : MatchDetailUIState()
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

    fun toggleMatchDetails(matchId: String) {
        if (_matchDetailUIState.value is MatchDetailUIState.All &&
                (_matchDetailUIState.value as MatchDetailUIState.All).selectedMatchId == matchId) {
                //same card clicked again, just collapse it
                _matchDetailUIState.value = MatchDetailUIState.Empty
                return
        }
        fetchMatchDetails(matchId)
    }

    private fun fetchMatchDetails(matchId: String) {
        _matchDetailUIState.value = MatchDetailUIState.Loading(matchId)
        viewModelScope.launch {
            repository.fetchMatchDetails(matchId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("MatchesViewModel::fetchMatchDetails", e.toString())
                    emit(emptyList())
                }
                .collect { statistics ->
                    if (statistics.isNotEmpty()) {
                        _matchDetailUIState.value = MatchDetailUIState.All(matchId)
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