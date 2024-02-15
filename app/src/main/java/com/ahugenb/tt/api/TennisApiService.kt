package com.ahugenb.tt.api

import com.ahugenb.tt.match.response.MatchResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers

interface TennisApiService {
    @Headers(
        "X-RapidAPI-Key:a2c9ab7299msh337cd8de85e4a05p12ac18jsn61fc75a39eff",
        "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com"
    )
    @GET("live_scores")
    suspend fun getLiveScores(): MatchResponseWrapper
}