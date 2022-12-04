package com.belkanoid.secretchat.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object ApiFactory {
    private const val BASE_URL = "http://89.22.229.252:8080/"

    private val client = OkHttpClient().newBuilder()
        .followRedirects(true)
        .addInterceptor(MessangerInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    val service = retrofit.create(ApiService::class.java)

}