package com.ahugenb.tt.match.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahugenb.tt.match.MatchListUIState
import com.ahugenb.tt.match.MatchViewModel
import com.ahugenb.tt.match.domain.Match

@Composable
fun MatchesScreen(viewModel: MatchViewModel = hiltViewModel()) {
    val state = viewModel.matchesState.collectAsStateWithLifecycle().value

    when(state) {
        MatchListUIState.Empty -> {
            Text("No Live Match Data Available")
        }
        MatchListUIState.Loading -> {
            Text("Loading")
        }
        is MatchListUIState.All -> {
            MatchesList(matches = state.matches)
        }
    }
}

@Composable
fun MatchesList(matches: List<Match>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(matches) { match ->
            MatchItem(match)
        }
    }
}

@Composable
fun MatchItem(match: Match) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${match.tournament} - ${match.round}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${match.homePlayer} vs ${match.awayPlayer}")
            Text(
                text = "Surface: ${match.surface}")
            Text(
                text = "Current Set: ${match.currentSet}, " +
                        "Scores: ${match.homeScore} - ${match.awayScore}",)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Sets: ${formatSets(match.setsHomePlayer)} vs ${formatSets(match.setsAwayPlayer)}",)
        }
    }
}

@Composable
fun formatSets(sets: List<String>): String {
    return sets.joinToString(separator = " ")
}