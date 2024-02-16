package com.ahugenb.tt.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahugenb.tt.R
import com.ahugenb.tt.home.NavRoutes.ATP_RANKINGS
import com.ahugenb.tt.home.NavRoutes.HIGHLIGHTED_PLAYER
import com.ahugenb.tt.home.NavRoutes.ITEM_ID
import com.ahugenb.tt.home.NavRoutes.MATCH_DETAILS
import com.ahugenb.tt.home.NavRoutes.MATCH_LIST
import com.ahugenb.tt.home.NavRoutes.TOURNAMENTS
import com.ahugenb.tt.home.NavRoutes.WTA_RANKINGS
import com.ahugenb.tt.home.NavRoutes.getDetailsArgumentRoute
import com.ahugenb.tt.match.detail.screen.MatchDetailScreen
import com.ahugenb.tt.match.list.screen.MatchesScreen


@Composable
fun HomeNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MATCH_LIST) {
        composable(HIGHLIGHTED_PLAYER) { Text(stringResource(R.string.tab_highlighted_player)) }
        composable(TOURNAMENTS) { Text(stringResource(R.string.tab_tournaments)) }
        composable(MATCH_LIST) {
            MatchesScreen(onNavigateToDetail = { itemId ->
                navController.navigate(getDetailsArgumentRoute(itemId))
            })
        }
        composable(
            "${MATCH_DETAILS}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.StringType } )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString(ITEM_ID) ?: return@composable
            MatchDetailScreen(itemId = itemId)
        }
        composable(ATP_RANKINGS) { Text(stringResource(R.string.tab_atp_rankings)) }
        composable(WTA_RANKINGS) { Text(stringResource(R.string.tab_wta_rankings)) }

    }
}