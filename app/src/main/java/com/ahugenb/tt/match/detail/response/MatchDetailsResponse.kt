package com.ahugenb.tt.match.detail.response

import com.google.gson.annotations.SerializedName

data class MatchDetailsResponse(
    @SerializedName("Match Id") val matchId: String?,
    @SerializedName("statistics") val statistics: List<Statistic>
)

data class Statistic(
    @SerializedName("Match ID") val matchID: String?,
    @SerializedName("P1 Aces") val p1Aces: String?,
    @SerializedName("P1 Break points converted") val p1BreakPointsConverted: String?,
    @SerializedName("P1 Break points saved") val p1BreakPointsSaved: String?,
    @SerializedName("P1 Double faults") val p1DoubleFaults: String?,
    @SerializedName("P1 First serve") val p1FirstServe: String?,
    @SerializedName("P1 First serve points") val p1FirstServePoints: String?,
    @SerializedName("P1 First serve return points") val p1FirstServeReturnPoints: String?,
    @SerializedName("P1 Max games in a row") val p1MaxGamesInARow: String?,
    @SerializedName("P1 Max points in a row") val p1MaxPointsInARow: String?,
    @SerializedName("P1 Receiver points won") val p1ReceiverPointsWon: String?,
    @SerializedName("P1 Return games played") val p1ReturnGamesPlayed: String?,
    @SerializedName("P1 Second serve") val p1SecondServe: String?,
    @SerializedName("P1 Second serve points") val p1SecondServePoints: String?,
    @SerializedName("P1 Second serve return points") val p1SecondServeReturnPoints: String?,
    @SerializedName("P1 Service games played") val p1ServiceGamesPlayed: String?,
    @SerializedName("P1 Service games won") val p1ServiceGamesWon: String?,
    @SerializedName("P1 Service points won") val p1ServicePointsWon: String?,
    @SerializedName("P1 Tiebreaks") val p1Tiebreaks: String?,
    @SerializedName("P1 Total") val p1Total: String?,
    @SerializedName("P1 Total won") val p1TotalWon: String?,
    @SerializedName("P1 name") val p1Name: String?,
    @SerializedName("P2 Aces") val p2Aces: String?,
    @SerializedName("P2 Break points converted") val p2BreakPointsConverted: String?,
    @SerializedName("P2 Break points saved") val p2BreakPointsSaved: String?,
    @SerializedName("P2 Double faults") val p2DoubleFaults: String?,
    @SerializedName("P2 First serve") val p2FirstServe: String?,
    @SerializedName("P2 First serve points") val p2FirstServePoints: String?,
    @SerializedName("P2 First serve return points") val p2FirstServeReturnPoints: String?,
    @SerializedName("P2 Max games in a row") val p2MaxGamesInARow: String?,
    @SerializedName("P2 Max points in a row") val p2MaxPointsInARow: String?,
    @SerializedName("P2 Receiver points won") val p2ReceiverPointsWon: String?,
    @SerializedName("P2 Return games played") val p2ReturnGamesPlayed: String?,
    @SerializedName("P2 Second serve") val p2SecondServe: String?,
    @SerializedName("P2 Second serve points") val p2SecondServePoints: String?,
    @SerializedName("P2 Second serve return points") val p2SecondServeReturnPoints: String?,
    @SerializedName("P2 Service games played") val p2ServiceGamesPlayed: String?,
    @SerializedName("P2 Service games won") val p2ServiceGamesWon: String?,
    @SerializedName("P2 Service points won") val p2ServicePointsWon: String?,
    @SerializedName("P2 Tiebreaks") val p2Tiebreaks: String?,
    @SerializedName("P2 Total") val p2Total: String?,
    @SerializedName("P2 Total won") val p2TotalWon: String?,
    @SerializedName("P2 name") val p2Name: String?
)