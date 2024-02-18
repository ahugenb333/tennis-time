package com.ahugenb.tt.match.list.model

import com.ahugenb.tt.match.list.model.domain.Match
import com.ahugenb.tt.match.list.model.domain.ServingState
import com.ahugenb.tt.match.list.model.domain.SetScore
import com.ahugenb.tt.match.list.model.domain.Tournament
import com.ahugenb.tt.match.list.model.response.MatchResponse
import kotlin.math.roundToInt

class MatchListUtils {
    companion object {

        //convert European decimal odds to American moneyline odds
        private fun Double.toAmericanOdds(): String {
            if (this > 2.0) {
                val rounded = roundToNearest5((this - 1.0) * 100)
                return "+${rounded}"// Underdog
            } else {
                val rounded = roundToNearest5(100.0 / (this - 1.0))
                return "-${rounded}" // Favorite
            }
        }

        private fun roundToNearest5(num: Double): Int {
            return (num / 5.0).roundToInt() * 5
        }

        fun MatchResponse.toDomainMatch(): Match {
            // Parse set scores and handle tiebreak logic within each set
            val sets = listOf(
                parseSetScores(set1Player1, set1Player2),
                parseSetScores(set2Player1, set2Player2),
                parseSetScores(set3Player1, set3Player2),
                parseSetScores(set4Player1, set4Player2),
                parseSetScores(set5Player1, set5Player2)
            ).filterNot { it.gamesHomePlayer == 0 && it.gamesAwayPlayer == 0 }


            // Determine the current set number
            val currentSetInt = currentSet.toIntOrNull() ?: 0

            // Determine serving state
            val servingState = getServingState(sets, firstToServe, currentSetInt)

            // Parse tournament details
            val parsedTournament = parseTournament(tournament, round)

            return Match(
                id = id,
                homePlayer = homePlayer,
                awayPlayer = awayPlayer,
                sets = sets,
                currentSet = currentSetInt,
                servingState = servingState,
                homeScore = player1Score,
                awayScore = player2Score,
                surface = surface,
                tournament = parsedTournament,
                liveHomeOdd = liveHomeOdd.toAmericanOdds(),
                liveAwayOdd = liveAwayOdd.toAmericanOdds(),
                initialHomeOdd = initialHomeOdd.toAmericanOdds(),
                initialAwayOdd = initialAwayOdd.toAmericanOdds()
            )
        }

        private fun parseTournament(tournament: String, round: String): Tournament {
            var updatedRound = round
            if (tournament.endsWith(", Qualifying")) {
                updatedRound = "Qualifying"
            }

            // Attempt to split the tournament string into city and country components.
            val parts = tournament.removeSuffix(", Qualifying").split(", ").toMutableList()
            if (parts.size > 1) {
                // Attempt to identify and remove repeated city names.
                val cityParts = parts[0].split(" ").toMutableList()
                val midPoint = cityParts.size / 2
                if (cityParts.size % 2 == 0) {
                    val firstHalf = cityParts.subList(0, midPoint).joinToString(" ")
                    val secondHalf = cityParts.subList(midPoint, cityParts.size).joinToString(" ")
                    if (firstHalf == secondHalf) {
                        parts[0] = firstHalf // Update to use only the first half if it's a repeat.
                    }
                }
            }

            val tournamentName = if (parts.isNotEmpty()) parts[0] else tournament
            val location = if (parts.size > 1) parts[1] else ""

            val finalName =
                if (!tournamentName.contains(location)) {
                    "$tournamentName, $location"
                } else {
                    tournamentName
                }

            return Tournament(
                name = finalName,
                round = updatedRound
            )
        }

        private fun parseSetScores(homeScoreStr: String, awayScoreStr: String): SetScore {
            val wentToTieBreak = homeScoreStr.contains("(") || awayScoreStr.contains("(")

            val homeGames = homeScoreStr.filter { it.isDigit() }.toIntOrNull() ?: 0
            val awayGames = awayScoreStr.filter { it.isDigit() }.toIntOrNull() ?: 0

            var tieBreakLoserScore: Int? = null
            var totalTiebreakPoints: Int? = null

            if (wentToTieBreak) {
                val pattern = "\\((\\d+)\\)".toRegex()
                val homeTieBreakScore = pattern.find(homeScoreStr)?.groupValues?.getOrNull(1)?.toIntOrNull()
                val awayTieBreakScore = pattern.find(awayScoreStr)?.groupValues?.getOrNull(1)?.toIntOrNull()

                // Assuming the player with the higher game score wins the tiebreak
                tieBreakLoserScore = if (homeGames > awayGames) awayTieBreakScore else homeTieBreakScore
                totalTiebreakPoints = homeTieBreakScore?.plus(awayTieBreakScore ?: 0)
            }

            return SetScore(
                gamesHomePlayer = homeGames,
                gamesAwayPlayer = awayGames,
                wentToTieBreak = wentToTieBreak,
                tieBreakLoserScore = tieBreakLoserScore,
                totalTieBreakPoints = totalTiebreakPoints
            )
        }
        private fun getServingState(
            sets: List<SetScore>,
            firstToServe: Int?,
            currentSet: Int
        ): ServingState {

            if (firstToServe == null) return ServingState.NONE

            val currentSetIndex = currentSet - 1
            val currentSetScore = sets.getOrNull(currentSetIndex)

            // Handle tiebreak logic
            if (currentSetScore?.wentToTieBreak == true) {
                val tiebreakPointsPlayed = currentSetScore.totalTieBreakPoints ?: 0 // Use tiebreak points

                // Determine initial server in the tiebreak (based on who served first in the regular set)
                val initialServerInTiebreak =
                    if (sets.take(currentSet).sumOf { it.gamesHomePlayer + it.gamesAwayPlayer } % 2 == 0) {
                        firstToServe
                    } else {
                        3 - firstToServe
                    }

                val isHomeServing = ((tiebreakPointsPlayed + initialServerInTiebreak) % 4) in 0..1
                return if (isHomeServing) ServingState.HOME_IS_SERVING else ServingState.AWAY_IS_SERVING
            }

            // Regular game serving logic
            val gamesInCurrentSet = sets.take(currentSet).sumOf { it.gamesHomePlayer + it.gamesAwayPlayer }
            val isHomeServing = (gamesInCurrentSet + firstToServe) % 2 == 1
            return if (isHomeServing) ServingState.HOME_IS_SERVING else ServingState.AWAY_IS_SERVING
        }
    }
}