package com.ahugenb.tt.match.detail.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahugenb.tt.match.detail.MatchDetailViewModel

@Composable
fun MatchDetailScreen(viewModel: MatchDetailViewModel = hiltViewModel(), itemId: String) {
    Text("Match Details: $itemId")
}