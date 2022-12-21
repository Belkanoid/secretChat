package com.belkanoid.secretchat.data.dto

import com.google.gson.annotations.SerializedName

data class UserBody(
    val id: Long = 0,
    val name: String,
    val pkey:String,
    val token:String
)
