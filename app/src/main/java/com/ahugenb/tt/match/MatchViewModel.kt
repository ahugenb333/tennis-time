package com.ahugenb.tt.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahugenb.tt.api.TennisRepository
import com.ahugenb.tt.match.response.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface IMatchViewModel {
    val matches: StateFlow<MatchListUIState>
    fun fetchMatches()
}

sealed class MatchListUIState {
    data object Empty : MatchListUIState()
    data class All(val matches: List<Match>): MatchListUIState()
}

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: TennisRepository
): ViewModel(), IMatchViewModel {

    private val _matches = MutableStateFlow<MatchListUIState>(MatchListUIState.Empty)
    override val matches: StateFlow<MatchListUIState> = _matches

    init {
        fetchMatches()
    }

    override fun fetchMatches() = viewModelScope.launch {
        val fetchedMatches = repository.getMatches()
        if (fetchedMatches.)
    }
}