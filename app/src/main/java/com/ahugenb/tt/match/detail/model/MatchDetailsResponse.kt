package com.ahugenb.tt.match.detail.model

import com.google.gson.annotations.SerializedName

data class MatchDetailsResponse(
    @SerializedName("Match Id") val matchId: String,
    @SerializedName("statistics") val statistics: List<Statistic>
)

data class Statistic(
    @SerializedName("Aces P1") val acesP1: String,
    @SerializedName("Aces P2") val acesP2: String,
    @SerializedName("Break points converted P1") val breakPointsConvertedP1: String,
    @SerializedName("Break points converted P2") val breakPointsConvertedP2: String,
    @SerializedName("Break points saved P1") val breakPointsSavedP1: String,
    @SerializedName("Break points saved P2") val breakPointsSavedP2: String,
    @SerializedName("First serve P1") val firstServeP1: String,
    @SerializedName("First serve P2") val firstServeP2: String,
    @SerializedName("First serve points P1") val firstServePointsP1: String,
    @SerializedName("First serve points P2") val firstServePointsP2: String,
    @SerializedName("First serve return points P1") val firstServeReturnPointsP1: String,
    @SerializedName("First serve return points P2") val firstServeReturnPointsP2: String,
    @SerializedName("Max games in a row P1") val maxGamesInRowP1: String,
    @SerializedName("Max games in a row P2") val maxGamesInRowP2: String,
    @SerializedName("P1 name") val p1Name: String,
    @SerializedName("P2 name") val p2Name: String,
    @SerializedName("Second serve P1") val secondServeP1: String,
    @SerializedName("Second serve P2") val secondServeP2: String,
    @SerializedName("Second serve points P1") val secondServePointsP1: String,
    @SerializedName("Second serve points P2") val secondServePointsP2: String,
    @SerializedName("Second serve return points P1") val secondServeReturnPointsP1: String,
    @SerializedName("Second serve return points P2") val secondServeReturnPointsP2: String,
    @SerializedName("Service games played P1") val serviceGamesPlayedP1: String,
    @SerializedName("Service games played P2") val serviceGamesPlayedP2: String,
    @SerializedName("Service points won P1") val servicePointsWonP1: String,
    @SerializedName("Service points won P2") val servicePointsWonP2: String,
    @SerializedName("Tiebreaks P1") val tiebreaksP1: String,
    @SerializedName("Tiebreaks P2") val tiebreaksP2: String,
    @SerializedName("Tournament") val tournament: String
)