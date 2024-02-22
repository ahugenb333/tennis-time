package com.ahugenb.tt.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 3559|oeLTO37MWYcP6BQ5mEJMjZDIHy49z2mZlrpUphBP")
            .build()
        return chain.proceed(request)
    }
}