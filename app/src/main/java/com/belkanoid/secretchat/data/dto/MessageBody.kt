package com.belkanoid.secretchat.data.dto

import com.google.gson.annotations.SerializedName


data class MessageBody(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("reciever")val receiver: Long,
    @SerializedName("sender")val sender: Long,
    @SerializedName("text")val text: String,
    @SerializedName("timestamp")val timestamp: Long,
    @SerializedName("edited")val edited: Boolean
)
