package com.ahugenb.tt.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TennisApiService {
    @Headers("X-RapidAPI-Key: YOUR_API_KEY", "X-RapidAPI-Host: ultimate-tennis1.p.rapidapi.com")
    @GET("match_details/{matchId}")
    suspend fun getMatchDetails(@Query("matchId") matchId: String): MatchDetailResponse

    companion object {
        private const val BASE_URL = "https://ultimate-tennis1.p.rapidapi.com/"

        fun create(): TennisApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TennisApiService::class.java)
        }
    }
}