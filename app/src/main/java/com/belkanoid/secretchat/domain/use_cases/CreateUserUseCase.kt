package com.belkanoid.secretchat.domain.use_cases

import com.belkanoid.secretchat.domain.repository.MessengerRepository

class CreateUserUseCase(private val repository: MessengerRepository) {
    suspend operator fun invoke(userName: String): Long{
        return repository.createUser(userName = userName)
    }
}