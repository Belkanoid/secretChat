package com.belkanoid.secretchat.data.repository

import android.util.Log
import com.belkanoid.secretchat.data.dto.MessageBody
import com.belkanoid.secretchat.data.dto.UserBody
import com.belkanoid.secretchat.data.network.ApiFactory
import com.belkanoid.secretchat.data.util.SharedPreferences
import com.belkanoid.secretchat.data.util.SharedPreferences.Companion.PRIVATE_KEY
import com.belkanoid.secretchat.data.util.SharedPreferences.Companion.PUBLIC_KEY
import com.belkanoid.secretchat.data.util.SharedPreferences.Companion.TOKEN
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.entity.User
import com.belkanoid.secretchat.domain.repository.MessengerRepository
import com.belkanoid.secretchat.domain.rsa.RsaKey
import com.belkanoid.secretchat.presentation.Injector
import okio.ByteString.decodeBase64
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class MessengerRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : MessengerRepository {

    private val service = ApiFactory.service
    private val rsa = RsaKey(sharedPreferences)

    override suspend fun sendMessage(
        receiver: Long,
        sender: Long,
        message: String,
        isEdited: Boolean
    ): Boolean {
        val receiverPublicKey = Injector.get().getInteractor().getUserPublicKey(receiver)

        Log.d("KEYTT", "$receiver: ${receiverPublicKey.PublicKey == sharedPreferences.getString(PUBLIC_KEY)}")
        val encryptedMessage = rsa.encryptMessage(message, receiverPublicKey.PublicKey)
        val postBody = MessageBody(
            receiver = receiver,
            sender = sender,
            text = encryptedMessage,
            timestamp = Date().time,
            edited = isEdited
        )
        service.sendMessage(postBody).enqueue(
            object : retrofit2.Callback<MessageBody> {
                override fun onResponse(call: Call<MessageBody>, response: Response<MessageBody>) {
                    Log.d(
                        "isSuccessful on CreateUser",
                        response.message() + " " + response.isSuccessful
                    )
                }
                override fun onFailure(call: Call<MessageBody>, t: Throwable) {

                    Log.d("isError on SendMessage", t.toString())
                }

            }
        )
        return true
    }

    override suspend fun getQueue(userId: Long): List<Queue> {
        val token = sharedPreferences.getString(TOKEN)
        val response = service.getQueue(
            userId = userId,
            token = token
        )
        return response.body()
            ?: throw RuntimeException("Queue is: ${response.body()} and ${response.isSuccessful}")
    }

    override suspend fun getMessage(messageId: Long): Message {
        val token = sharedPreferences.getString(TOKEN)
        val privateKey = sharedPreferences.getString(PRIVATE_KEY)
        val response = service.getMessage(
            messageId = messageId,
            token = token
        )
        return try {
            val message = response.body()!!

            val decryptedMessage = rsa.decryptMessage(message.message, privateKey)
            message.copy(message = decryptedMessage)

        }catch(e: Exception) {
            Log.d("Error on getMessage","Message is: ${response.body()} and ${response.isSuccessful}")
            Message(0,0,0,"",0,false)
        }

    }

    override suspend fun createUser(userName: String) {
        val token = UUID.randomUUID().toString().replace("-", "")
        sharedPreferences.putString(TOKEN, token)
        val publicKey = sharedPreferences.getString(PUBLIC_KEY)
        Log.d("KEYS ON CREATE USER", publicKey)

        val userBody = UserBody(
            name = userName,
            pkey = publicKey,
            token = token
        )

        service.createUser(userBody).enqueue(
            object : retrofit2.Callback<UserBody> {
                override fun onResponse(call: Call<UserBody>, response: Response<UserBody>) {
                    val userId = response.body()?.id ?: -1L
                    Injector.get().getInteractor().saveUserId(userId)

                    Log.d(
                        "isSuccessful on CreateUser",
                        response.message() + " " + response.isSuccessful
                    )
                }

                override fun onFailure(call: Call<UserBody>, t: Throwable) {
                    Log.d("isError on CreateUser", t.toString())
                }
            }
        )
    }

    override suspend fun getUser(userId: Long): User {
        val response = service.getUser(userId = userId)
        return response.body() ?: User(-1L, "Пользователь не найден", "", "")
    }

}