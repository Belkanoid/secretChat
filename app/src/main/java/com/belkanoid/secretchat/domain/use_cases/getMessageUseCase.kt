package com.belkanoid.secretchat.domain.use_cases

import androidx.lifecycle.LiveData
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.repository.MessengerRepository

class getMessageUseCase(private val repository: MessengerRepository) {

    suspend operator fun invoke(messageId: Long): Message {
        return repository.getMessage(messageId = messageId)
    }
}