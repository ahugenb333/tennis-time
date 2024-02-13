package com.ahugenb.tt.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState

    fun onNavigationItemSelected(screen: Screen) {
        _screenState.value = _screenState.value.copy(currentScreen = screen)
    }
}

enum class Screen {
    HIGHLIGHTED_PLAYER, TOURNAMENTS, LIVE_MATCHES, ATP_RANKINGS, WTA_RANKINGS
}

data class ScreenState(
    val currentScreen: Screen = Screen.LIVE_MATCHES
)