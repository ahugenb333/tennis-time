package com.ahugenb.tt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahugenb.tt.R

object HomeNavRoutes {
    const val HIGHLIGHTED_PLAYER = "highlightedPlayer"
    const val TOURNAMENTS = "tournaments"
    const val LIVE_MATCHES = "liveMatches"
    const val ATP_RANKINGS = "atpRankings"
    const val WTA_RANKINGS = "wtaRankings"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomNavItems = listOf(
        HomeNavRoutes.HIGHLIGHTED_PLAYER to R.string.tab_highlighted_player,
        HomeNavRoutes.TOURNAMENTS to R.string.tab_tournaments,
        HomeNavRoutes.LIVE_MATCHES to R.string.tab_live_matches,
        HomeNavRoutes.ATP_RANKINGS to R.string.tab_atp_rankings,
        HomeNavRoutes.WTA_RANKINGS to R.string.tab_wta_rankings
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentPadding = PaddingValues(0.dp),
                scrollBehavior = scrollBehavior
            ) {
                bottomNavItems.forEach { (route, labelRes) ->
                    val isSelected = currentRoute == route
                    BottomNavigationItem(
                        label = stringResource(id = labelRes),
                        isSelected = isSelected,
                        onClick = {
                            // Navigate to the associated screen
                            // Use this navigation logic to avoid creating a new instance of the screen if it's already on top of the nav stack
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    // This will clear all entries on top of the target destination and launch it
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when navigating back to the top-level destination
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeNavHost(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                RectangleShape
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        )
    }
}