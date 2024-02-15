package com.ahugenb.tt.match

import com.ahugenb.tt.match.domain.Match
import com.ahugenb.tt.match.domain.ServingState
import com.ahugenb.tt.match.domain.SetScore
import com.ahugenb.tt.match.response.MatchResponse

class MatchUtils {
    companion object {
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

            return Match(
                id = id,
                homePlayer = homePlayer,
                awayPlayer = awayPlayer,
                sets = sets,
                currentSet = currentSetInt,
                servingState = servingState,
                homeScore = player1Score,
                awayScore = player2Score,
                round = round,
                tournament = tournament,
                surface = surface
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