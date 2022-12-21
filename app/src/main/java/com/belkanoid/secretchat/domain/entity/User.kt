package com.belkanoid.secretchat.domain.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("pkey")
    val PublicKey:String,
    @SerializedName("token")
    val token:String,
)
