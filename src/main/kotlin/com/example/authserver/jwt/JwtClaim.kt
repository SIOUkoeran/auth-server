package com.example.authserver.jwt

data class JwtClaim(
    val username: String,
    val email: String,
    val userId: Long,
    val role : String
)
