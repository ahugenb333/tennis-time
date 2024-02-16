package com.ahugenb.tt.match.list.model.domain

data class Match(
    val id: String,
    val homePlayer: String,
    val awayPlayer: String,
    val sets: List<SetScore>,
    val currentSet: Int,
    val servingState: ServingState, // This will be determined based on the logic we implement
    val homeScore: String,
    val awayScore: String,
    val round: String,
    val tournament: String,
    val surface: String
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

