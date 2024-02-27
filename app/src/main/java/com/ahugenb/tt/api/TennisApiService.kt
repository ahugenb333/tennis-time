package com.ahugenb.tt.api

import com.ahugenb.tt.match.list.model.response.MatchDetailsResponse
import com.ahugenb.tt.match.list.model.response.MatchListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TennisApiService {

    @GET("786/fetch+live+tennis+matches")
    suspend fun getLiveScores(): MatchListResponse

    @GET("787/fetch+tennis+match+statistics")
    suspend fun getMatchDetails(@Query(value = "match_id") matchId: String): MatchDetailsResponse
}