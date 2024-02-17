package com.ahugenb.tt.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahugenb.tt.R
import com.ahugenb.tt.home.NavRoutes.ATP_RANKINGS
import com.ahugenb.tt.home.NavRoutes.HIGHLIGHTED_PLAYER
import com.ahugenb.tt.home.NavRoutes.MATCH_LIST
import com.ahugenb.tt.home.NavRoutes.TOURNAMENTS
import com.ahugenb.tt.home.NavRoutes.WTA_RANKINGS
import com.ahugenb.tt.match.list.screen.MatchListScreen


@Composable
fun HomeNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MATCH_LIST) {
        composable(HIGHLIGHTED_PLAYER) { Text(stringResource(R.string.tab_highlighted_player)) }
        composable(TOURNAMENTS) { Text(stringResource(R.string.tab_tournaments)) }
        composable(MATCH_LIST) { MatchListScreen() }
        composable(ATP_RANKINGS) { Text(stringResource(R.string.tab_atp_rankings)) }
        composable(WTA_RANKINGS) { Text(stringResource(R.string.tab_wta_rankings)) }

    }
}