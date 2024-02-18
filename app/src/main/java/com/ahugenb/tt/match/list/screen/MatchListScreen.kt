package com.ahugenb.tt.match.list.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahugenb.tt.common.BouncingBallLoader
import com.ahugenb.tt.match.MatchDetailUIState
import com.ahugenb.tt.match.MatchListUIState
import com.ahugenb.tt.match.MatchViewModel
import com.ahugenb.tt.match.detail.response.Statistic
import com.ahugenb.tt.match.list.model.domain.Match
import com.ahugenb.tt.match.list.model.domain.ServingState
import com.ahugenb.tt.match.list.model.domain.SetScore

@Composable
fun MatchListScreen(
    viewModel: MatchViewModel = hiltViewModel()
) {
    val matchListState = viewModel.matchListUIState.collectAsStateWithLifecycle().value
    val matchDetailState = viewModel.matchDetailUIState.collectAsStateWithLifecycle().value

    when (matchListState) {
        MatchListUIState.Empty -> {
            Text("No Live Match Data Available")
        }

        MatchListUIState.Loading -> {
            BouncingBallLoader()
        }

        is MatchListUIState.All -> {
            MatchList(
                matches = matchListState.matches,
                matchDetailState,
                viewModel::fetchMatches,
                viewModel::fetchMatchDetails
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchList(
    matches: List<Match>,
    matchDetailState: MatchDetailUIState,
    onRefresh: () -> Unit,
    onMatchClicked: (String) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val selectedMatchId = rememberSaveable { mutableStateOf("") }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            onRefresh()
        }
    }
    Box(Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(matches) { match ->
                MatchItem(match, selectedMatchId.value, matchDetailState, onMatchClicked = { id ->
                    if (id == selectedMatchId.value) {
                        //collapse re-clicked item
                        selectedMatchId.value = ""
                    } else {
                        //expand item
                        selectedMatchId.value = id
                    }
                    onMatchClicked(id)
                })
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState
        )
    }
}

@Composable
fun MatchItem(
    match: Match,
    selectedMatchId: String,
    matchDetailState: MatchDetailUIState,
    onMatchClicked: (String) -> Unit
) {
    // Expanded state for the card
    val isExpanded = selectedMatchId == match.id
    // Arrow rotation animation
    val arrowRotationDegree by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = ""
    )

    val cardElevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp,
        pressedElevation = 8.dp,
        focusedElevation = 4.dp,
        hoveredElevation = 6.dp
    )

    Card(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onMatchClicked(match.id) },
        elevation = cardElevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = match.tournament.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${match.tournament.round} (${match.surface})",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
                val sets = formatSets(match.sets)
                Text(
                    text = "$sets | ${match.homeScore} - ${match.awayScore}",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = 2.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Player names and match info in two columns
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = match.homePlayer,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Start
                        )
                        if (match.servingState === ServingState.HOME_IS_SERVING) {
                            Spacer(modifier = Modifier.size(4.dp))
                            ServeIndicator()
                        }
                    }
                    Text(
                        text = "Live Odds: ${match.liveHomeOdd}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "Initial Odds: ${match.initialHomeOdd}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (match.servingState === ServingState.AWAY_IS_SERVING) {
                            ServeIndicator()
                            Spacer(modifier = Modifier.size(4.dp))
                        }
                        Text(
                            text = match.awayPlayer,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.End
                        )
                    }
                    Text(
                        text = "Live Odds: ${match.liveAwayOdd}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "Initial Odds: ${match.initialAwayOdd}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                }
            }
            if (match.id == selectedMatchId) {
                when (matchDetailState) {
                    is MatchDetailUIState.Loading -> {
                        CenteredProgressIndicator()
                    }

                    is MatchDetailUIState.Populated -> {
                        MatchStatistics(match.statistic)
                    }

                    else -> {}
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onMatchClicked(match.id) },
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(arrowRotationDegree)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand or collapse match details"
                    )
                }
            }
        }
    }
}

@Composable
fun ServeIndicator() {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.size(10.dp)) {
        drawCircle(color = color)
    }
}

@Composable
fun CenteredProgressIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MatchStatistics(statistic: Statistic?) {
    if (statistic != null) {
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
    }
}

private fun formatSets(sets: List<SetScore>): String {
    return sets.joinToString {
        if (it.tieBreakLoserScore != null) {
            val homePlayerWon = it.gamesHomePlayer > it.gamesAwayPlayer
            if (homePlayerWon) {
                "${it.gamesHomePlayer}-${it.gamesAwayPlayer}(${it.tieBreakLoserScore})"
            } else {
                "${it.gamesHomePlayer}(${it.tieBreakLoserScore})-${it.gamesAwayPlayer}"
            }
        } else {
            "${it.gamesHomePlayer}-${it.gamesAwayPlayer}"
        }
    }
}

private fun formatAwaySets(sets: List<SetScore>): String {
    return sets.joinToString {
        it.gamesAwayPlayer.toString()
    }
}