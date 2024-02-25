package com.ahugenb.tt.api.tennis

import com.ahugenb.tt.tournament.model.AtpTournamentsResponse
import com.ahugenb.tt.tournament.model.WtaTournamentsReponse
import retrofit2.http.GET
import retrofit2.http.Path

enum class Category(val path: String) {
    ATPGS("atpgs"),
    ATP("atp"),
    GRANDSLAM("gs"),
    THOUSAND("1000"),
    CHALLENGER("ch"),
    NONE("")
}
interface TennisApiService {

    @GET("tournament_list/atp/{year}/{category}")
    suspend fun getAtpTournamentList(@Path("year") year: Int, @Path("category") category: String): AtpTournamentsResponse

    @GET("tournament_list/wta/{year}")
    suspend fun getWtaTournamentList(@Path("year") year: Int): WtaTournamentsReponse

}