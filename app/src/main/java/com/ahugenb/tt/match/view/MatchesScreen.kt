package com.ahugenb.tt.match.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
            MatchesList(matches = state.matches, viewModel::fetchMatches)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesList(matches: List<Match>, onRefresh: () -> Unit) {
    val pullToRefreshState = rememberPullToRefreshState()

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            onRefresh()
        }
    }
    Box(Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(matches) { match ->
                MatchItem(match)
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState
        )
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

fun formatSets(sets: List<String>): String {
    return sets.joinToString(separator = " ")
}