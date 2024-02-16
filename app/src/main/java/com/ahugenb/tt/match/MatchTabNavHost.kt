package com.ahugenb.tt.match

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahugenb.tt.match.detail.screen.MatchDetailScreen
import com.ahugenb.tt.match.list.screen.MatchesScreen


object MatchAppRoutes {
    const val SCREEN_LIST = "screenList"
    const val SCREEN_DETAILS = "screenDetails/{itemId}" // Using a dynamic argument for item ID
    fun screenDetailsRoute(itemId: String) = "screenDetails/$itemId"
}
@Composable
fun MatchTabNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = MatchAppRoutes.SCREEN_LIST) {
        composable(MatchAppRoutes.SCREEN_LIST) {
            MatchesScreen(onNavigateToDetail = { itemId ->
                navController.navigate(MatchAppRoutes.screenDetailsRoute(itemId))
            })
        }
        composable(
            MatchAppRoutes.SCREEN_DETAILS,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType } )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            MatchDetailScreen(itemId = itemId)

        }
    }
}