package com.ahugenb.tt.tournament.model

import com.google.gson.annotations.SerializedName

data class AtpTournamentsResponse(
    @SerializedName("Category")
    val category: String,

    @SerializedName("Tournaments")
    val tournaments: List<AtpTournament>,

    @SerializedName("Year")
    val year: String
)

data class AtpTournament(
    @SerializedName("DblDrawSize")
    val dblDrawSize: Int,

    @SerializedName("FormattedDate")
    val formattedDate: String,

    @SerializedName("Id")
    val id: String,

    @SerializedName("IndoorOutdoor")
    val indoorOutdoor: String,

    @SerializedName("Location")
    val location: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("SglDrawSize")
    val sglDrawSize: Int,

    @SerializedName("Surface")
    val surface: String,

    @SerializedName("TotalFinancialCommitment")
    val totalFinancialCommitment: String?,

    @SerializedName("TournamentSiteUrl")
    val tournamentSiteUrl: String,

    @SerializedName("Type")
    val type: String
)