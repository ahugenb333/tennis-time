package com.ahugenb.tt.match.domain

data class Match(
    val id: String,
    val homePlayer: String,
    val awayPlayer: String,
    val currentSet: String,
    val firstToServe: Int,
    val homeScore: String,
    val awayScore: String,
    val setsHomePlayer: Int,
    val setsAwayPlayer: Int,
    val tournament: String,
    val surface: String
)
