package com.belkanoid.secretchat.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MessangerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest : Request = chain.request()
        val newUrl : HttpUrl = originalRequest.url().newBuilder()
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .header("Content-Type", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}