package com.ahugenb.tt.api

import com.ahugenb.tt.match.response.MatchResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers

interface TennisApiService {
    @Headers(
        "X-RapidAPI-Key:YOUR_KEY_HERE",
        "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com"
    )
    @GET("live_scores")
    suspend fun getLiveScores(): MatchResponseWrapper
}