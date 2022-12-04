package com.belkanoid.secretchat.domain.use_cases

import androidx.lifecycle.LiveData
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.repository.MessengerRepository

class sendMessageUseCase(private val repository: MessengerRepository) {

    suspend operator fun invoke(receiver: Long, sender: Long, message: String, isEdited: Boolean = false): Boolean {
        return repository.sendMessage(receiver, sender, message, isEdited)
    }
}
