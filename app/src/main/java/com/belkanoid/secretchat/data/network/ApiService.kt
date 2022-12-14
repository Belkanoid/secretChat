package com.belkanoid.secretchat.data.network

import com.belkanoid.secretchat.data.dto.MessageBody
import com.belkanoid.secretchat.data.dto.UserBody
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.entity.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

//    @Headers("Content-Type: application/json")
    @POST("users")
    fun createUser(@Body postUserBody: UserBody): Call<UserBody>

//    @Headers("Content-Type: application/json")
    @POST("messages")
    fun sendMessage(@Body postMessageBody: MessageBody): Call<MessageBody>

    @GET("messages/{id}")
    suspend fun getMessage(@Path("id") messageId: Long, @Query("token") token: String): Response<Message>

    @GET("quaue/{id}")
    suspend fun getQueue(@Path("id") userId: Long, @Query("token") token: String): Response<List<Queue>>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Long): Response<User>



}