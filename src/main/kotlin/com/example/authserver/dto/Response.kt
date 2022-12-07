package com.example.authserver.dto

import com.example.authserver.model.User
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
data class Response(
    val code : Int,
    val message : String,
    val data : Any,
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
data class ResponseUser(
    val username : String,
    val email : String,
    val userId : Long?,
    val createdAt : LocalDateTime?,
    val updatedAt : LocalDateTime? = null
){
    constructor(user : User) : this(
        user.username,
        user.email,
        user.id,
        user.createAt,
        user.updatedAt
    )
}


