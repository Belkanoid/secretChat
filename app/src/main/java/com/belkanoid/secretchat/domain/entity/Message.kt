package com.belkanoid.secretchat.domain.entity

import com.google.gson.annotations.SerializedName

data class Message (
    @SerializedName("id")
    val id: Int,
    @SerializedName("reciever")
    val receiver: Int,
    @SerializedName("sender")
    val sender: Int,
    @SerializedName("text")
    val message: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("edited")
    val isEdited: Boolean
)