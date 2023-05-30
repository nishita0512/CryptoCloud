package com.example.cryptocloud.models

data class CryptoFile(

    val name: String,
    val url: String,
    val fileType: String,
    val timeCreated: Long,
    val ownerName: String,
    val hashedPassword: String

)
