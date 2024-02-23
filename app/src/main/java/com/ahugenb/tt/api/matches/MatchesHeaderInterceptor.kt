package com.ahugenb.tt.api.matches

import okhttp3.Interceptor
import okhttp3.Response

class MatchesHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 3575|nWtle9aCXt9ky67W7cK9reqf695ZtYZjXD5ZXzNh")
            .build()
        return chain.proceed(request)
    }
}