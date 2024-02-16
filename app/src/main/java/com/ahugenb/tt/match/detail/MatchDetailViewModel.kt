package com.ahugenb.tt.match.detail

import androidx.lifecycle.ViewModel
import com.ahugenb.tt.api.TennisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


sealed class MatchDetailUIState {

    data object Empty : MatchDetailUIState()

    data object Loading : MatchDetailUIState()

//    data class All(val matchDetails: MatchDetails): MatchDetailUIState()
}


@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val repository: TennisRepository
): ViewModel() {

    private val _matchDetailUIState = MutableStateFlow<MatchDetailUIState>(MatchDetailUIState.Loading)
    val matchDetailUIState: StateFlow<MatchDetailUIState> = _matchDetailUIState

}