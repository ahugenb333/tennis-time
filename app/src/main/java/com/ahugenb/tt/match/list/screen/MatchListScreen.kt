package com.ahugenb.tt.match.list.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.ahugenb.tt.match.list.model.domain.Match
import com.ahugenb.tt.match.list.model.domain.ServingState
import com.ahugenb.tt.match.list.model.domain.SetScore
import com.ahugenb.tt.match.list.model.response.Statistic
import com.ahugenb.tt.match.list.model.response.hasAnyNonNullProperty

enum class DropdownOption(val label: String) {
    ALL("All"),
    SINGLES("Singles"),
    DOUBLES("Doubles")
}

val dropdownList = listOf(
    DropdownOption.ALL,
    DropdownOption.SINGLES,
    DropdownOption.DOUBLES
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchListScreen() {
    val viewModel: MatchViewModel = hiltViewModel()
    val matchListState = viewModel.matchListUIState.collectAsStateWithLifecycle().value
    val matchDetailState = viewModel.matchDetailUIState.collectAsStateWithLifecycle().value

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    //persist dropdown selection
    val dropdownSelection = rememberSaveable { mutableStateOf(dropdownList[0]) }
    //used to close the expanded item if we select a different dropdown option
    val didDropdownChange = rememberSaveable { mutableStateOf(false) }

    val forgetItem = didDropdownChange.value
    if (forgetItem) {
        //reset for next time
        didDropdownChange.value = false
    }

    when (matchListState) {
        is MatchListUIState.Loading -> {
            Scaffold(
                topBar = { DummyTopBar() }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    BouncingBallLoader()
                }
            }
        }

        is MatchListUIState.All -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { TitleText() },
                        actions = {
                            MatchDropdownMenu(
                                currentSelection = dropdownSelection.value,
                                onDropdownOptionSelected = { selected ->
                                    didDropdownChange.value = dropdownSelection.value != selected
                                    dropdownSelection.value = selected
                                }
                            )
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            scrolledContainerColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    PullToRefreshContent(
                        matchListState = matchListState,
                        matchDetailState = matchDetailState,
                        dropdownSelection = dropdownSelection.value,
                        didDropdownChange = forgetItem,
                        onRefresh = viewModel::fetchMatches,
                        onMatchClicked = viewModel::fetchMatchDetails
                    )
                }
            }
        }

        else -> {
            PullToRefreshContent(
                matchListState = matchListState,
                matchDetailState = matchDetailState,
                dropdownSelection = dropdownSelection.value,
                onRefresh = viewModel::fetchMatches,
                onMatchClicked = viewModel::fetchMatchDetails
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyTopBar() {
    CenterAlignedTopAppBar(
        title = { TitleText() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            scrolledContainerColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
fun TitleText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                textAlign = TextAlign.Center,
                text = "Live Matches",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row {
            Text(
                textAlign = TextAlign.Center,
                text = "(pull down to refresh)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun MatchDropdownMenu(
    currentSelection: DropdownOption,
    onDropdownOptionSelected: (DropdownOption) -> Unit
) {
    //don't persist dropdown expanded
    var dropdownExpanded by remember { mutableStateOf(false) }

    val arrowRotationDegree by animateFloatAsState(
        targetValue = if (dropdownExpanded) 180f else 0f,
        label = ""
    )

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .clickable { dropdownExpanded = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = currentSelection.label,
                color = MaterialTheme.colorScheme.primary,

                modifier = Modifier
                    .wrapContentWidth(Alignment.End)
                    .padding(16.dp)
            )
        }
        Column {
            IconButton(
                onClick = { dropdownExpanded = !dropdownExpanded },
                modifier = Modifier
                    .size(28.dp)
                    .rotate(arrowRotationDegree)
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand or collapse action bar"
                )
            }
        }
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            dropdownList.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    onClick = {
                        onDropdownOptionSelected(option)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}

//enables refreshing on empty state as well as match list state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshContent(
    matchListState: MatchListUIState,
    matchDetailState: MatchDetailUIState,
    dropdownSelection: DropdownOption,
    didDropdownChange: Boolean = false,
    onRefresh: () -> Unit,
    onMatchClicked: (String) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            onRefresh()
        }
    }

    Row {
        Box(
            Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            when (matchListState) {
                is MatchListUIState.Empty -> {
                    EmptyState()
                }

                is MatchListUIState.All -> {
                    MatchList(
                        matchListState.matches,
                        matchDetailState,
                        dropdownSelection,
                        didDropdownChange,
                        onMatchClicked
                    )
                }

                else -> {}
            }
            PullToRefreshContainer(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .nestedScroll(pullToRefreshState.nestedScrollConnection),
                state = pullToRefreshState
            )
        }
    }
}

@Composable
fun EmptyState() {
    //potentially hacky, would normally be Column but it needs to be scrollable to trigger the Pull to refresh
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(1) {
            Text(
                text = "No Live Match Data Available",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "(Pull down to check again)",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun MatchList(
    matches: List<Match>,
    matchDetailState: MatchDetailUIState,
    dropdownSelection: DropdownOption,
    didDropdownChange: Boolean,
    onMatchClicked: (String) -> Unit
) {
    val selectedMatchId = rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    val visibleItemRange by remember {
        derivedStateOf {
            val firstVisible = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            firstVisible..lastVisible
        }
    }

    if (didDropdownChange) {
        selectedMatchId.value = ""
    }

    val displayMatches = when (dropdownSelection) {
        DropdownOption.ALL -> {
            matches
        }

        DropdownOption.DOUBLES -> {
            matches.filter { it.isDoubles }
        }

        DropdownOption.SINGLES -> {
            matches.filter { !it.isDoubles }
        }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(displayMatches) { index, match ->
            val isVisible = index in visibleItemRange
            MatchItem(match, isVisible, selectedMatchId.value, matchDetailState, onMatchClicked = { id ->
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
}

@Composable
fun MatchItem(
    match: Match,
    isVisible: Boolean,
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

    val modifier = if (isVisible) Modifier.animateContentSize() else Modifier

    Card(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onMatchClicked(match.id) },
        elevation = cardElevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
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
                    text = "$sets | ${match.homeScore}-${match.awayScore}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                top = 2.dp,
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
    val color = MaterialTheme.colorScheme.secondary
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
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun MatchStatistics(statistic: Statistic?) {
    if (statistic != null && statistic.hasAnyNonNullProperty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.onTertiaryContainer, shape = RoundedCornerShape(4.dp))
        ) {
            Column {

                // Here we define the labels and the corresponding values for each player
                val statisticsMap = listOf(
                    "Aces" to (statistic.p1Aces to statistic.p2Aces),
                    "Break points converted" to (statistic.p1BreakPointsConverted to statistic.p2BreakPointsConverted),
                    "Break points saved" to (statistic.p1BreakPointsSaved to statistic.p2BreakPointsSaved),
                    "Double faults" to (statistic.p1DoubleFaults to statistic.p2DoubleFaults),
                    "First serve return points" to (statistic.p1FirstServeReturnPoints to statistic.p2FirstServeReturnPoints),
                    "Max points in a row" to (statistic.p1MaxPointsInARow to statistic.p2MaxPointsInARow),
                    "Return points won" to (statistic.p1ReceiverPointsWon to statistic.p2ReceiverPointsWon),
                    "Return games played" to (statistic.p1ReturnGamesPlayed to statistic.p2ReturnGamesPlayed),
                    "Second serve return points" to (statistic.p1SecondServeReturnPoints to statistic.p2SecondServeReturnPoints),
                    "Service games played" to (statistic.p1ServiceGamesPlayed to statistic.p2ServiceGamesPlayed),
                    "Service games won" to (statistic.p1ServiceGamesWon to statistic.p2ServiceGamesWon),
                    "Service points won" to (statistic.p1ServicePointsWon to statistic.p2ServicePointsWon),
                    "Tiebreaks" to (statistic.p1Tiebreaks to statistic.p2Tiebreaks),
                    "Total points won" to (statistic.p1Total to statistic.p2Total)
                )

                // Create a row for each statistic
                statisticsMap.forEachIndexed { index,  (label, values) ->
                    if (values.first != null && values.second != null) {
                        if (index > 0) {
                            // Add a colored spacer line between items
                            Spacer(modifier = Modifier
                                .background(MaterialTheme.colorScheme.onTertiaryContainer)
                                .height(1.dp)
                                .fillMaxWidth(),
                            )
                        }
                        StatisticRow(label, values.first, values.second)
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun StatisticRow(label: String, playerOneValue: String?, playerTwoValue: String?) {
    if (playerOneValue == null || playerTwoValue == null) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp) // Spacing between statistic rows
    ) {
        Text(
            text = playerOneValue,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp), // Space between text and central label
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
        Text(
            text = label,
            modifier = Modifier
                .weight(2f),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = playerTwoValue,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp), // Space between text and central label
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start
        )
    }
}

private fun formatSets(sets: List<SetScore>): String {
    val result = sets.joinToString {
        if (it.wentToTieBreak) {
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
    if (result.isEmpty() || result == " ") {
        return "0-0"
    }
    return result
}