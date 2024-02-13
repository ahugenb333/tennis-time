package com.ahugenb.tt.match

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.tt.api.TennisRepository
import com.ahugenb.tt.match.domain.Match
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
    data class All(val matches: List<Match>): MatchListUIState()
}

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: TennisRepository
): ViewModel() {

    private val _matchesState = MutableStateFlow<MatchListUIState>(MatchListUIState.Empty)
    val matchesState: StateFlow<MatchListUIState> = _matchesState

    init {
        fetchMatches()
    }

    private fun fetchMatches() {
        _matchesState.value = MatchListUIState.Loading
        viewModelScope.launch {
            repository.fetchMatches()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("MatchesViewModel::fetchMatches", e.toString())
                    emit(emptyList())
                }
                .collect { matches ->
                    _matchesState.value = if (matches.isNotEmpty()) {
                        MatchListUIState.All(matches)
                    } else {
                        MatchListUIState.Empty
                    }
                }
        }
    }
}