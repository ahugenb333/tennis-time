package com.ahugenb.tt.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", "f096da5312msheb450599d794275p150c45jsnd0679f3027a7")
            .addHeader("X-RapidAPI-Host", "ultimate-tennis1.p.rapidapi.com")
            .build()
        return chain.proceed(request)
    }
}