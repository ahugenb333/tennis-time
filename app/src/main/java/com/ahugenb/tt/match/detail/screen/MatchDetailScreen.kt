package com.ahugenb.tt.match.detail.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahugenb.tt.common.BouncingBallLoader
import com.ahugenb.tt.match.detail.MatchDetailUIState
import com.ahugenb.tt.match.detail.MatchDetailViewModel
import com.ahugenb.tt.match.detail.model.Statistic

@Composable
fun MatchDetailScreen(viewModel: MatchDetailViewModel = hiltViewModel(), itemId: String) {
    val uiState = viewModel.matchDetailUIState.collectAsStateWithLifecycle().value

    when (uiState) {
        is MatchDetailUIState.Loading -> {
            BouncingBallLoader()
        }

        is MatchDetailUIState.All -> {
            MatchStatistics(statistic = uiState.statistic)
        }

        is MatchDetailUIState.Empty -> {
            Text(text = "No data available")
        }
    }

    if (uiState is MatchDetailUIState.Loading) {
        viewModel.fetchMatchDetails(itemId)
    }
}

@Composable
fun MatchStatistics(statistic: Statistic) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Match Statistics", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "P1 Name: ${statistic.p1Name}")
        Text(text = "P2 Name: ${statistic.p2Name}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Aces P1: ${statistic.acesP1}")
        Text(text = "Aces P2: ${statistic.acesP2}")
        Text(text = "First Serve P1: ${statistic.firstServeP1}")
        Text(text = "First Serve P2: ${statistic.firstServeP2}")
        Text(text = "First Serve Points P1: ${statistic.firstServePointsP1}")
        Text(text = "First Serve Points P2: ${statistic.firstServePointsP2}")
        Text(text = "Second Serve P1: ${statistic.secondServeP1}")
        Text(text = "Second Serve P2: ${statistic.secondServeP2}")
        Text(text = "Break Points Converted P1: ${statistic.breakPointsConvertedP1}")
        Text(text = "Break Points Converted P2: ${statistic.breakPointsConvertedP2}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Break Points Saved P1: ${statistic.breakPointsSavedP1}")
        Text(text = "Break Points Saved P2: ${statistic.breakPointsSavedP2}")
        Text(text = "Service Games Played P1: ${statistic.serviceGamesPlayedP1}")
        Text(text = "Service Games Played P2: ${statistic.serviceGamesPlayedP2}")
        Text(text = "Tournament: ${statistic.tournament}")
    }
}