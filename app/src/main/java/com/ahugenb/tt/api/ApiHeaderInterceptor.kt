package com.ahugenb.tt.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", "")
            .addHeader("X-RapidAPI-Host", "ultimate-tennis1.p.rapidapi.com")
            .build()
        return chain.proceed(request)
    }
}