package com.ahugenb.tt.match.list.model.domain

import com.ahugenb.tt.match.detail.response.Statistic

data class Match(
    val id: String,
    val homePlayer: String,
    val awayPlayer: String,
    val sets: List<SetScore>,
    val currentSet: Int,
    val servingState: ServingState,
    val homeScore: String,
    val awayScore: String,
    val round: String,
    val tournament: String,
    val surface: String,
    val liveHomeOdd: Int,
    val liveAwayOdd: Int,
    val initialHomeOdd: Int,
    val initialAwayOdd: Int,
    var statistic: Statistic? = null
)
data class SetScore(
    val gamesHomePlayer: Int,
    val gamesAwayPlayer: Int,
    val wentToTieBreak: Boolean,
    val tieBreakLoserScore: Int?,
    val totalTieBreakPoints: Int?
)

enum class ServingState {
    HOME_IS_SERVING,
    AWAY_IS_SERVING,
    NONE
}

