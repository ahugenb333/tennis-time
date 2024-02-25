package com.ahugenb.tt.tournament

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TournamentScreen() {
    val viewModel: TournamentViewModel = hiltViewModel()
    val uiState = viewModel.tournamentListUIState.collectAsStateWithLifecycle().value

    when (uiState) {
        is TournamentListUIState.Atp -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.tournaments.size, key = { index -> uiState.tournaments[index].id } ) { index -> 
                    val tournament = uiState.tournaments[index]
                    val fieldList = listOf(
                        "Name: ${tournament.name}",
                        "Location: ${tournament.location}",
                        "Surface: ${tournament.surface}",
                        "Prize money: ${tournament.totalFinancialCommitment}",
                        "Dates: ${tournament.formattedDate}",
                        "Url: ${tournament.tournamentSiteUrl}",
                        "Singles: ${tournament.sglDrawSize}",
                        "Doubles: ${tournament.dblDrawSize}",
                        "Type: ${tournament.type}"
                    )
                    for (item in fieldList) {
                        TournamentLine(item)
                    }

                    Spacer(
                        modifier = Modifier.height(1.dp).fillMaxWidth().background(color = MaterialTheme.colorScheme.onPrimary)
                    )
                    
                }
            }
        }
        is TournamentListUIState.Wta -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.tournaments.size, key = { index -> uiState.tournaments[index].id } ) { index ->
                    val tournament = uiState.tournaments[index]
                    val fieldList = listOf(
                        "Name: ${tournament.fullName}",
                        "Location: ${tournament.city}",
                        "Surface: ${tournament.surface}",
                        "Prize money: ${tournament.prizeMoney}",
                        "Dates: ${tournament.startDate} - ${tournament.endDate}",
                        "Singles: ${tournament.doublesDrawSize}",
                        "Doubles: ${tournament.singlesDrawSize}",
                        "Outdoor/indoor: ${tournament.outdoorIndoor}"
                    )
                    for (item in fieldList) {
                        TournamentLine(item)
                    }

                    Spacer(
                        modifier = Modifier.height(1.dp).fillMaxWidth().background(color = MaterialTheme.colorScheme.onPrimary)
                    )

                }
            }

        }
        else -> { Text("none") }
    }
}

@Composable
fun TournamentLine(text: String) {
    Row {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}