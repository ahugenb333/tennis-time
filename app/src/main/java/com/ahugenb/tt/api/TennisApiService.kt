package com.ahugenb.tt.api

import com.ahugenb.tt.match.response.MatchResponseList
import retrofit2.http.GET
import retrofit2.http.Headers

interface TennisApiService {
    @Headers(
        "X-RapidAPI-Key: 21ba053232msh9a56089f81cc9adp1cf4edjsn6027b2418943",
        "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com"
    )
    @GET("live_scores")
    suspend fun getMatches(): ApiResponse<MatchResponseList>
}