package com.ahugenb.tt.tournament

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahugenb.tt.tournament.model.Tournament

@Composable
fun TournamentScreen() {
    val viewModel: TournamentViewModel = hiltViewModel()
    val uiState = viewModel.tournamentListUIState.collectAsStateWithLifecycle().value

    when (uiState) {
        is TournamentListUIState.All -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.tournaments.size, key = { index -> uiState.tournaments[index].id } ) { index -> 
                    val tournament = uiState.tournaments[index]
                    TournamentItem(tournament = tournament)
                    
                }
            }
        }
        else -> { Text("none") }
    }
}

@Composable
fun TournamentItem(tournament: Tournament) {
    Column {
        Row {
            Text("location: $tournament.location")
        }
        Row {
            Text("surface: $tournament.surface")
        }
        Row {
            Text("timestamp: $tournament.timestamp")
        }
        Row {
            Text("prizemoney: ${tournament.totalPrizeMoney}")
        }
        Row {
            Text("name: ${tournament.tournamentName}")
        }
    }
}