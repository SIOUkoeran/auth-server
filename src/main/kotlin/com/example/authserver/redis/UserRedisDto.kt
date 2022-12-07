package com.example.authserver.redis

import com.example.authserver.model.User
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime


data class UserRedisDto(

    @JsonProperty("username")
    val username : String,
    @JsonProperty("email")
    val email : String,
    @JsonProperty("id")
    val id : Long?,
    @JsonProperty("created_at")
    val createdAt : LocalDateTime?= null,
    @JsonProperty("updated_at")
    val updatedAt : LocalDateTime? = null
){
    constructor(user : User) : this(
        user.username,
        user.email,
        user.id,
        user.createAt!!,
        user.updatedAt
    )
}
