package com.belkanoid.secretchat.domain.repository

import android.text.BoringLayout
import androidx.lifecycle.LiveData
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.entity.User

interface MessengerRepository {

    suspend fun sendMessage(receiver: Long, sender: Long, message: String, isEdited: Boolean): Boolean

    suspend fun getMessage(messageId: Long): Message

    suspend fun getQueue(userId: Long): List<Queue>

    suspend fun createUser(userName: String)

    suspend fun getUser(userId: Long): User
}