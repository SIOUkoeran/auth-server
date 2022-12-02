package com.example.authserver.exception

sealed class CustomException(
    val code : Int,
    override val message : String,
) : RuntimeException(message)

data class UserExistException(
    val errorCode: ErrorCode = ErrorCode.EXIST_USER
) : CustomException(errorCode.code, errorCode.message)

