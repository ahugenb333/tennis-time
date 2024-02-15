package com.ahugenb.tt.api

import com.ahugenb.tt.match.response.MatchResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers

interface TennisApiService {
    @Headers(
        "X-RapidAPI-Key:3e50af0721msh73e1f3688b33897p11cfb8jsn9c9d9f0f0b2d",
        "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com"
    )
    @GET("live_scores")
    suspend fun getLiveScores(): MatchResponseWrapper
}