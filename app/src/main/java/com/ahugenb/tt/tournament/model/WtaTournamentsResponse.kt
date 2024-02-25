package com.ahugenb.tt.tournament.model

import com.google.gson.annotations.SerializedName

data class WtaTournamentsReponse(
    @SerializedName("Tournaments")
    val tournaments: List<WtaTournament>,

    @SerializedName("Year")
    val year: Int
)

data class WtaTournament(
    @SerializedName("Full Name")
    val fullName: String,

    @SerializedName("ID")
    val id: String,

    @SerializedName("Outdoor/Indoor")
    val outdoorIndoor: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("doublesDrawSize")
    val doublesDrawSize: Int,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("prizeMoney")
    val prizeMoney: Int,

    @SerializedName("prizeMoneyCurrency")
    val prizeMoneyCurrency: String,

    @SerializedName("singlesDrawSize")
    val singlesDrawSize: Int,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("surface")
    val surface: String,

    @SerializedName("year")
    val year: Int
)
