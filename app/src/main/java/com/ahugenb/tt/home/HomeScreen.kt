package com.ahugenb.tt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahugenb.tt.R
import com.ahugenb.tt.home.NavRoutes.ATP_RANKINGS
import com.ahugenb.tt.home.NavRoutes.HIGHLIGHTED_PLAYER
import com.ahugenb.tt.home.NavRoutes.MATCH_DETAILS
import com.ahugenb.tt.home.NavRoutes.MATCH_LIST
import com.ahugenb.tt.home.NavRoutes.TOURNAMENTS
import com.ahugenb.tt.home.NavRoutes.WTA_RANKINGS

object NavRoutes {
    const val HIGHLIGHTED_PLAYER = "highlightedPlayer"
    const val TOURNAMENTS = "tournaments"
    const val MATCH_LIST = "matchList"
    const val ATP_RANKINGS = "atpRankings"
    const val WTA_RANKINGS = "wtaRankings"
    const val MATCH_DETAILS = "matchDetails"
    const val ITEM_ID = "itemId"
    fun getDetailsArgumentRoute(itemId: String) = "$MATCH_DETAILS/$itemId"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomNavItems = listOf(
        HIGHLIGHTED_PLAYER to R.string.tab_highlighted_player,
        TOURNAMENTS to R.string.tab_tournaments,
        MATCH_LIST to R.string.tab_live_matches,
        ATP_RANKINGS to R.string.tab_atp_rankings,
        WTA_RANKINGS to R.string.tab_wta_rankings
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.secondary,
            ) {
                if (currentRoute != null && !currentRoute.startsWith(MATCH_DETAILS)) {
                    bottomNavItems.forEach { (route, labelRes) ->
                        val isSelected = currentRoute == route
                        BottomNavigationItem(
                            modifier = Modifier.weight(1f),
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
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                RectangleShape
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            softWrap = true,
            textAlign = TextAlign.Center,
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        )
    }
}