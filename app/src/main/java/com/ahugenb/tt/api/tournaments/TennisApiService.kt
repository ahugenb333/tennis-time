package com.ahugenb.tt.api.tournaments

import com.ahugenb.tt.tournament.model.TournamentsResponse
import retrofit2.http.GET
import retrofit2.http.Path


enum class Tour(val path: String) {
    ATP("atp"),
    WTA("wta")
}

enum class Category(val path: String) {
    ATPGS("atpgs"),
    ATP("atp"),
    GRANDSLAM("gs"),
    THOUSAND("1000"),
    CHALLENGER("ch"),
    NONE("")
}
interface TennisApiService {

    @GET("tournament_list/{tour}/{year}/{category}")
    suspend fun getTournamentList(@Path("tour") tour: String, @Path("year") year: Int, @Path("category") category: String): TournamentsResponse
}