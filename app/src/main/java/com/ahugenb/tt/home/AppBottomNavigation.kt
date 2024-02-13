package com.ahugenb.tt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahugenb.tt.R

@Composable
fun AppBottomNavigation(currentScreen: Screen, onNavigationItemSelected: (Screen) -> Unit) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                label = stringResource(R.string.tab_highlighted_player),
                isSelected = currentScreen == Screen.HIGHLIGHTED_PLAYER,
                onClick = { onNavigationItemSelected(Screen.HIGHLIGHTED_PLAYER) },
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            BottomNavigationItem(
                label = stringResource(R.string.tab_tournaments),
                isSelected = currentScreen == Screen.TOURNAMENTS,
                onClick = { onNavigationItemSelected(Screen.TOURNAMENTS) },
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            BottomNavigationItem(
                label = stringResource(R.string.tab_live_matches),
                isSelected = currentScreen == Screen.LIVE_MATCHES,
                onClick = { onNavigationItemSelected(Screen.LIVE_MATCHES) },
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            BottomNavigationItem(
                label = stringResource(R.string.tab_atp_rankings),
                isSelected = currentScreen == Screen.ATP_RANKINGS,
                onClick = { onNavigationItemSelected(Screen.ATP_RANKINGS) },
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            BottomNavigationItem(
                label = stringResource(R.string.tab_wta_rankings),
                isSelected = currentScreen == Screen.WTA_RANKINGS,
                onClick = { onNavigationItemSelected(Screen.WTA_RANKINGS) },
                modifier = Modifier.weight(1f)
            )
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
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary, RectangleShape)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        )
    }
}