package com.ahugenb.tt.match.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.tt.api.TennisRepository
import com.ahugenb.tt.match.detail.model.Statistic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class MatchDetailUIState {

    data object Empty : MatchDetailUIState()

    data object Loading : MatchDetailUIState()

    data class All(val statistic: Statistic): MatchDetailUIState()
}


@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val repository: TennisRepository
): ViewModel() {

    private val _matchDetailUIState = MutableStateFlow<MatchDetailUIState>(MatchDetailUIState.Loading)
    val matchDetailUIState: StateFlow<MatchDetailUIState> = _matchDetailUIState

    fun fetchMatchDetails(matchId: String) {
        viewModelScope.launch {
            repository.fetchMatchDetails(matchId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("MatchesViewModel::fetchMatches", e.toString())
                    emit(emptyList())
                }
                .collect { statistics ->
                    _matchDetailUIState.value =
                        if (statistics.isNotEmpty())
                            MatchDetailUIState.All(statistics[0])
                        else MatchDetailUIState.Empty
                }
        }
    }
}