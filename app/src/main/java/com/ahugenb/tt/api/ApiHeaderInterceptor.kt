package com.ahugenb.tt.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 3541|sdUPYbY0N1w5MBz9TmI4fCjN8JcdYFBWTKA8Ao4z")
            .build()
        return chain.proceed(request)
    }
}