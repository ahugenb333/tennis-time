package com.ahugenb.tt.api

import com.ahugenb.tt.match.list.response.MatchResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers

interface TennisApiService {
    @Headers(
        "X-RapidAPI-Key:882e1df796mshbeb1b1a3d609fb2p1412a7jsn205e546ea64b",
        "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com"
    )
    @GET("live_scores")
    suspend fun getLiveScores(): MatchResponseWrapper


}