package com.example.authserver.dto

data class RequestLogin(
    val username : String,
    val password: String,
    val email:String
)

data class ResponseLogin(
    val username: String,
    val email : String,
    val userId : Long,
    val accessToken : String? = null,
    val refreshToken : String? = null,
)
