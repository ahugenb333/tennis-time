package com.ahugenb.tt.match.list.model

import com.ahugenb.tt.match.list.model.domain.Match
import com.ahugenb.tt.match.list.model.domain.MatchTournament
import com.ahugenb.tt.match.list.model.domain.ServingState
import com.ahugenb.tt.match.list.model.domain.SetScore
import com.ahugenb.tt.match.list.model.response.MatchResponse
import kotlin.math.roundToInt

class MatchListUtils {
    companion object {
        private fun Double.toAmericanOdds(): String {
            // Check for extremely low decimal odds, which could indicate an error or an edge case.
            if (this <= 1.01) return "-∞" // Indicating extremely high favoritism, practical limit.
            if (this >= 1000.0) return "+∞" // Indicating extreme underdog, practical limit.

            return if (this > 2.0) {
                // Underdog
                val value = (this - 1.0) * 100
                val rounded = roundToNearest5(value)
                "+$rounded"
            } else {
                // Favorite
                val value = 100.0 / (this - 1.0)
                val rounded = roundToNearest5(value)
                // Adding a safeguard for extremely high favorite values leading to negative infinity.
                if (rounded <= -2147483640) "-∞" else "-$rounded"
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
                homePlayer = homePlayer.parseDoublesName(),
                awayPlayer = awayPlayer.parseDoublesName(),
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
                isDoubles = homePlayer.parseDoublesName() != homePlayer,
                initialAwayOdd = initialAwayOdd.toAmericanOdds()
            )
        }

        private fun String.parseDoublesName(): String {
            if (!this.contains(" / ")) return this
            return this.replace(" / ", "\n")
        }

        private fun parseTournament(tournament: String, round: String): MatchTournament {
            var updatedRound = round
            if (tournament.contains("Qualifying")) {
                updatedRound = "Qualifying"
            }

            return MatchTournament(
                name = formatTournamentName(tournament.replace("Qualifying", "")),
                round = updatedRound
            )
        }

        private fun formatTournamentName(input: String): String {
            var formattedName = input

            val words = formattedName.split(",", " ")

            val distinctWords = words.distinct()
            formattedName = distinctWords.joinToString(" ")

            formattedName.replace(",,", ",")
                .replace(", ,", ",")
                .trimEnd(',')
                .trim()

            return formattedName.replace("\\s+".toRegex(), " ")
        }

        private fun parseSetScores(homeScoreStr: String, awayScoreStr: String): SetScore {
            val wentToTieBreak = homeScoreStr.contains("(") || awayScoreStr.contains("(")

            val homeGames = parseGames(scoreString = homeScoreStr)
            val awayGames = parseGames(scoreString = awayScoreStr)

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

        private fun parseGames(scoreString: String): Int {
            return if (scoreString == "None" || scoreString.isEmpty()) {
                0
            } else {
                scoreString[0].digitToInt()
            }
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