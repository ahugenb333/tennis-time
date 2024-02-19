package com.ahugenb.tt.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 3521|gHTeOaNFLFSNgzenm3AelDxwzwZu5HDHTHg9mqqr")
            .build()
        return chain.proceed(request)
    }
}