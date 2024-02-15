package com.ahugenb.tt.match

import androidx.annotation.VisibleForTesting
import com.ahugenb.tt.match.domain.Match
import com.ahugenb.tt.match.domain.ServingState
import com.ahugenb.tt.match.domain.SetScore
import com.ahugenb.tt.match.response.MatchResponse

class MatchUtils {
    companion object {
        fun MatchResponse.toDomainMatch(): Match {
            val sets = listOf(
                set1Player1 to set1Player2,
                set2Player1 to set2Player2,
                set3Player1 to set3Player2,
                set4Player1 to set4Player2,
                set5Player1 to set5Player2
            ).mapNotNull { (homeScore, awayScore) ->
                if (homeScore != "None" && awayScore != "None") {
                    val wentToTieBreak = homeScore.contains("(") || awayScore.contains("(")
                    val homeGames = homeScore.filter { it.isDigit() }.toIntOrNull() ?: 0
                    val awayGames = awayScore.filter { it.isDigit() }.toIntOrNull() ?: 0

                    val tieBreakLoserScore = try {
                        if (wentToTieBreak) {
                            val pattern = "\\((\\d+)\\)".toRegex()
                            if (homeGames > awayGames) {
                                pattern.find(awayScore)?.groupValues?.get(1)?.toInt()
                            } else {
                                pattern.find(homeScore)?.groupValues?.get(1)?.toInt()
                            }
                        } else null
                    } catch (e: Exception) {
                        null
                    }

                    SetScore(homeGames, awayGames, wentToTieBreak, tieBreakLoserScore)
                } else null
            }

            val currentSetInt = currentSet.toIntOrNull() ?: 1

            return Match(
                id = id,
                homePlayer = homePlayer,
                awayPlayer = awayPlayer,
                sets = sets,
                currentSet = currentSetInt,
                servingState = getServingState(sets, firstToServe, currentSetInt, player1Score, player2Score),
                homeScore = player1Score,
                awayScore = player2Score,
                round = round,
                tournament = tournament,
                surface = surface
            )
        }

        @VisibleForTesting
        private fun getServingState(
            sets: List<SetScore>,
            firstToServe: String,
            currentSet: Int,
            player1Score: String,
            player2Score: String
        ): ServingState {
            val parsedFirstToServe = firstToServe.toIntOrNull() ?: return ServingState.NONE
            val totalGamesPlayed =
                sets.sumOf { it.gamesHomePlayer + it.gamesAwayPlayer } +
                        calculateScoreValue(player1Score) +
                        calculateScoreValue(player2Score)
            val isTieBreak = sets.getOrNull(currentSet - 1)?.wentToTieBreak ?: false
            return if (isTieBreak) {
                // Tiebreak serving rule (simplified)
                if ((totalGamesPlayed + parsedFirstToServe) % 2 == 0) ServingState.HOME_IS_SERVING else ServingState.AWAY_IS_SERVING
            } else {
                // Regular serving rule
                if ((totalGamesPlayed + parsedFirstToServe) % 2 == 0) ServingState.HOME_IS_SERVING else ServingState.AWAY_IS_SERVING
            }
        }

        private fun calculateScoreValue(score: String): Int {
            return if (score == "A") {
                1 // Treat advantage as one point
            } else {
                score.toIntOrNull() ?: 0
            }
        }
    }
}