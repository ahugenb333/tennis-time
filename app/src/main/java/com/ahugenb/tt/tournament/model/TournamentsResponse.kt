package com.ahugenb.tt.tournament.model

import com.google.gson.annotations.SerializedName

data class TournamentsResponse(
    @SerializedName("Category")
    val category: String,
    @SerializedName("Tournaments")
    val tournaments: List<Tournament>,
    @SerializedName("Year")
    val year: String
)

data class Tournament(
    @SerializedName("ID")
    val id: String,
    @SerializedName("Location")
    val location: String,
    @SerializedName("Surface")
    val surface: String?,
    @SerializedName("Timestamp")
    val timestamp: String,
    @SerializedName("Total Prize Money")
    val totalPrizeMoney: String,
    @SerializedName("Tournament Name")
    val tournamentName: String,
    @SerializedName("Winners")
    val winners: Winners
)

data class Winners(
    @SerializedName("doubles")
    val doubles: String?,
    @SerializedName("singles")
    val singles: String?
)