package com.belkanoid.secretchat.domain.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("PublicKey")
    val PublicKey:String,
    @SerializedName("token")
    val token:String,
    @SerializedName("PrivateKey")
    val PrivateKey:String
)
