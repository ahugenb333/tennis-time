package com.ahugenb.tt.match.domain

data class Match(
    val id: String,
    val homePlayer: String,
    val awayPlayer: String,
    val currentSet: String, //todo convert to Int
    val isHomeServing: Boolean, //todo calculate, consider doubles, tiebreaks, etc
    val homeScore: String,
    val awayScore: String,
    val setsHomePlayer: List<String>,
    val setsAwayPlayer: List<String>,
    val round: String,
    val tournament: String,
    val surface: String
)
