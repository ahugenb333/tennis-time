package com.ahugenb.tt.api

import com.ahugenb.tt.match.detail.model.MatchDetailsResponse
import com.ahugenb.tt.match.list.model.response.MatchListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TennisApiService {

    @GET("live_scores")
    suspend fun getLiveScores(): MatchListResponse

    @GET("match_details/{matchId}")
    suspend fun getMatchDetails(@Path("matchId") matchId: String): MatchDetailsResponse
}