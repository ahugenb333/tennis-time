package com.ahugenb.tt.match

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahugenb.tt.match.MatchAppRoutes.ITEM_ID
import com.ahugenb.tt.match.MatchAppRoutes.SCREEN_MATCH_DETAILS
import com.ahugenb.tt.match.MatchAppRoutes.SCREEN_MATCH_LIST
import com.ahugenb.tt.match.MatchAppRoutes.getDetailsArgumentRoute
import com.ahugenb.tt.match.detail.screen.MatchDetailScreen
import com.ahugenb.tt.match.list.screen.MatchesScreen


object MatchAppRoutes {
    const val SCREEN_MATCH_LIST = "matchList"
    const val SCREEN_MATCH_DETAILS = "matchDetails"
    const val ITEM_ID = "itemId"
    fun getDetailsArgumentRoute(itemId: String) = "$SCREEN_MATCH_DETAILS/$itemId"
}
@Composable
fun MatchTabNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = SCREEN_MATCH_LIST) {
        composable(SCREEN_MATCH_LIST) {
            MatchesScreen(onNavigateToDetail = { itemId ->
                navController.navigate(getDetailsArgumentRoute(itemId))
            })
        }
        composable(
            "$SCREEN_MATCH_DETAILS/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.StringType } )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString(ITEM_ID) ?: return@composable
            MatchDetailScreen(itemId = itemId)

        }
    }
}