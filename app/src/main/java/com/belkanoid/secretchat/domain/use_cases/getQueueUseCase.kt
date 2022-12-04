package com.belkanoid.secretchat.domain.use_cases

import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.repository.MessengerRepository

class getQueueUseCase(private val repository: MessengerRepository) {
    suspend operator fun invoke(userId: Long): List<Queue> {
        return repository.getQueue(userId = userId)
    }
}