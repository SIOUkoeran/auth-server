package com.example.authserver.redis

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRedis(
    @JsonProperty("id")
    val id : Long,
    @JsonProperty("username")
    val username : String,
    @JsonProperty("email")
    val email : String
)