package com.belkanoid.secretchat.data.network

import androidx.lifecycle.LiveData
import com.belkanoid.secretchat.data.dto.MessageBody
import com.belkanoid.secretchat.data.dto.UserBody
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

//    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun createUser(@Body postUserBody: UserBody): Call<UserBody>

//    @Headers("Content-Type: application/json")
    @POST("messages")
    fun sendMessage(@Body postMessageBody: MessageBody): Call<MessageBody>

    @GET("messages/{id}")
    suspend fun getMessage(@Path("id") messageId: Long): Response<Message>

    @GET("quaue/{id}")
    suspend fun getQueue(@Path("id") userId: Long): Response<List<Queue>>



}