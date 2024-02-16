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
import com.ahugenb.tt.match.detail.screen.MatchDetailScreen
import com.ahugenb.tt.match.list.screen.MatchAppRoutes
import com.ahugenb.tt.match.list.screen.MatchesScreen


@Composable
fun HomeNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MatchAppRoutes.SCREEN_MATCH_LIST) {
        composable(HomeNavRoutes.HIGHLIGHTED_PLAYER) { Text(stringResource(R.string.tab_highlighted_player)) }
        composable(HomeNavRoutes.TOURNAMENTS) { Text(stringResource(R.string.tab_tournaments)) }
        composable(MatchAppRoutes.SCREEN_MATCH_LIST) {
            MatchesScreen(onNavigateToDetail = { itemId ->
                navController.navigate(MatchAppRoutes.getDetailsArgumentRoute(itemId))
            })
        }
        composable(
            "${MatchAppRoutes.SCREEN_MATCH_DETAILS}/{${MatchAppRoutes.ITEM_ID}}",
            arguments = listOf(navArgument(MatchAppRoutes.ITEM_ID) { type = NavType.StringType } )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString(MatchAppRoutes.ITEM_ID) ?: return@composable
            MatchDetailScreen(itemId = itemId)

        }
        composable(HomeNavRoutes.ATP_RANKINGS) { Text(stringResource(R.string.tab_atp_rankings)) }
        composable(HomeNavRoutes.WTA_RANKINGS) { Text(stringResource(R.string.tab_wta_rankings)) }

    }
}