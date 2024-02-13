package com.ahugenb.tt.match.response

import com.ahugenb.tt.match.domain.Match
import com.google.gson.annotations.SerializedName

data class MatchResponseWrapper(
    @SerializedName("matches")
    val matchResponse: List<MatchResponse>
)

data class MatchResponse(
    @SerializedName("Away Player")
    val awayPlayer: String,
    @SerializedName("Current set")
    val currentSet: String,
    @SerializedName("First to Serve")
    val firstToServe: Int,
    @SerializedName("Home Player")
    val homePlayer: String,
    @SerializedName("ID")
    val id: String,
    @SerializedName("Initial Away Odd")
    val initialAwayOdd: Double,
    @SerializedName("Initial Home Odd")
    val initialHomeOdd: Double,
    @SerializedName("Live Away Odd")
    val liveAwayOdd: Double,
    @SerializedName("Live Home Odd")
    val liveHomeOdd: Double,
    @SerializedName("Player 1 Score")
    val player1Score: String,
    @SerializedName("Player 2 Score")
    val player2Score: String,
    @SerializedName("Round")
    val round: String,
    @SerializedName("Set1 Player 1")
    val set1Player1: String,
    @SerializedName("Set1 Player 2")
    val set1Player2: String,
    @SerializedName("Set2 Player 1")
    val set2Player1: String,
    @SerializedName("Set2 Player 2")
    val set2Player2: String,
    @SerializedName("Set3 Player 1")
    val set3Player1: String,
    @SerializedName("Set3 Player 2")
    val set3Player2: String,
    @SerializedName("Set4 Player 1")
    val set4Player1: String,
    @SerializedName("Set4 Player 2")
    val set4Player2: String,
    @SerializedName("Set5 Player 1")
    val set5Player1: String,
    @SerializedName("Set5 Player 2")
    val set5Player2: String,
    @SerializedName("Sets Player 1")
    val setsPlayer1: Int,
    @SerializedName("Sets Player 2")
    val setsPlayer2: Int,
    @SerializedName("Surface")
    val surface: String,
    @SerializedName("Tournament")
    val tournament: String
)

fun MatchResponse.toDomainMatch(): Match {
    val setsHomePlayer = listOf(
        set1Player1,
        set2Player1,
        set3Player1,
        set4Player1,
        set5Player1
    ).filter {
        it != "None"
    }
    val setsAwayPlayer = listOf(
        set1Player2,
        set2Player2,
        set3Player2,
        set4Player2,
        set5Player2
    ).filter {
        it != "None"
    }
    return Match(
        id = id,
        homePlayer = homePlayer,
        awayPlayer = awayPlayer,
        currentSet = currentSet,
        isHomeServing = true,
        homeScore = player1Score,
        awayScore = player2Score,
        setsHomePlayer = setsHomePlayer,
        setsAwayPlayer = setsAwayPlayer,
        round = round,
        tournament = tournament,
        surface = surface
    )
}