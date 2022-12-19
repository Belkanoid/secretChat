package com.belkanoid.secretchat.data.dto

data class UserBody(
    val id: Long = 0,
    val name: String,
    val PublicKey:String,
    val token:String,
    val PrivateKey:String
)
