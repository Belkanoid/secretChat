package com.belkanoid.secretchat.data.repository

import android.util.Log
import com.belkanoid.secretchat.data.dto.MessageBody
import com.belkanoid.secretchat.data.dto.UserBody
import com.belkanoid.secretchat.data.network.ApiFactory
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.entity.User
import com.belkanoid.secretchat.domain.repository.MessengerRepository
import com.belkanoid.secretchat.presentation.Injector
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class MessengerRepositoryImpl @Inject constructor() : MessengerRepository {

    private val service = ApiFactory.service

    override suspend fun sendMessage(
        receiver: Long,
        sender: Long,
        message: String,
        isEdited: Boolean
    ): Boolean {
        val postBody = MessageBody(
            receiver = receiver,
            sender = sender,
            text = message,
            timestamp = Date().time,
            edited = isEdited
        )
        var response = true
        service.sendMessage(postBody).enqueue(
            object : retrofit2.Callback<MessageBody> {
                override fun onResponse(call: Call<MessageBody>, response: Response<MessageBody>) {
                }

                override fun onFailure(call: Call<MessageBody>, t: Throwable) {
                    response = false
                    Log.d("Error on SendMessage", t.toString())
                }

            }
        )
        return response
    }

    override suspend fun getQueue(userId: Long): List<Queue> {
        val response = service.getQueue(userId = userId)
        return response.body()
            ?: throw RuntimeException("Queue is: ${response.body()} and ${response.isSuccessful}")
    }

    override suspend fun getMessage(messageId: Long): Message {
        val response = service.getMessage(messageId)
        return response.body()
            ?: throw RuntimeException("Message is: ${response.body()} and ${response.isSuccessful}")
    }

    override suspend fun createUser(userName: String) {
        val userBody = UserBody(name = userName)
        service.createUser(userBody).enqueue(
            object : retrofit2.Callback<UserBody> {
                override fun onResponse(call: Call<UserBody>, response: Response<UserBody>) {
                    val userId = response.body()?.id ?: -1L
                    Injector.get().getInteractor().let {
                        it.saveUserId(userId)
                        runBlocking {
                            it.refreshQueue()
                        }
                    }

                    Log.d("Error on CreateUser", response.message() + " " + response.isSuccessful)
                }
                override fun onFailure(call: Call<UserBody>, t: Throwable) {
                    Log.d("Error on CreateUser", t.toString())
                }
            }
        )
    }

    override suspend fun getUser(userId: Long): User {
        val response = service.getUser(userId = userId)
        return response.body() ?: User(-1L, "Пользователь не найден")
    }
}