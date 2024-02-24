package com.ahugenb.tt.api.tennis

import okhttp3.Interceptor
import okhttp3.Response

class TennisHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", "API_KEY")
            .addHeader("X-RapidAPI-Host", "ultimate-tennis1.p.rapidapi.com")
            .build()
        return chain.proceed(request)
    }
}