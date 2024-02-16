package com.ahugenb.tt.api

import com.ahugenb.tt.match.list.response.MatchResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers

interface TennisApiService {
    @Headers(
        "X-RapidAPI-Key:c9fd45cf88mshe9e64be590a181dp1c8843jsn0132739f8e12",
        "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com"
    )
    @GET("live_scores")
    suspend fun getLiveScores(): MatchResponseWrapper


}