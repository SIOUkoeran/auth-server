package com.example.authserver.dto

import org.jetbrains.annotations.NotNull

data class Request(
    val email : String,
    val username : String
)

data class RequestAccount(

    @NotNull
    val email : String,
    @NotNull
    val username : String
)

data class RequestPasswordChange(
    @NotNull
    val password : String,
    @NotNull
    val email : String
)