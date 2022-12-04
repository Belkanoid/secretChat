package com.belkanoid.secretchat.domain.entity

import com.google.gson.annotations.SerializedName

data class Queue(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("messageId")
    val messageId: Int,
    @SerializedName("timestamp")
    val timestamp: Int
)