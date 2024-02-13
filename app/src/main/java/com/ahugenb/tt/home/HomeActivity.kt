package com.ahugenb.tt.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahugenb.tt.R
import com.ahugenb.tt.ui.theme.TennisTimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val mainState = viewModel.screenState.collectAsStateWithLifecycle().value

            TennisTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TennisTimeAppScreen(
                        currentScreen = mainState.currentScreen,
                        onNavigationItemSelected = viewModel::onNavigationItemSelected
                    )
                }
            }
        }
    }
}

@Composable
fun TennisTimeAppScreen(
    currentScreen: Screen,
    onNavigationItemSelected: (Screen) -> Unit
) {
    Scaffold(
        bottomBar = { AppBottomNavigation(currentScreen, onNavigationItemSelected) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.HIGHLIGHTED_PLAYER -> Text(stringResource(R.string.tab_highlighted_player))
                Screen.TOURNAMENTS -> Text(stringResource(R.string.tab_tournaments))
                Screen.LIVE_MATCHES -> Text(stringResource(R.string.tab_live_matches))
                Screen.ATP_RANKINGS -> Text(stringResource(R.string.tab_atp_rankings))
                Screen.WTA_RANKINGS -> Text(stringResource(R.string.tab_wta_rankings))
            }
        }

    }
}