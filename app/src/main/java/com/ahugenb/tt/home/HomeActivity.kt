package com.ahugenb.tt.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahugenb.tt.ui.theme.TennisTimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //MainViewModel ScreenState is used to manage the home screen tabs
            val viewModel: MainViewModel = viewModel()
            val mainState = viewModel.screenState.collectAsStateWithLifecycle().value

            TennisTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomAppBar(
                        currentScreen = mainState.currentScreen,
                        onNavigationItemSelected = viewModel::onNavigationItemSelected
                    )
                }
            }
        }
    }
}

