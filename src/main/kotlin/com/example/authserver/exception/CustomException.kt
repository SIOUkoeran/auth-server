package com.example.authserver.exception

sealed class CustomException(
    val code : Int,
    override val message : String,
) : RuntimeException(message)

data class AlreadyExistUserException(
    val errorCode: ErrorCode = ErrorCode.EXIST_USER
) : CustomException(errorCode.code, errorCode.message)

data class UserPasswordException(
    val errorCode: ErrorCode = ErrorCode.NOT_MATCH_PASSWORD
) : CustomException(errorCode.code, errorCode.message)

data class NotFoundUserNameException(
    val errorCode: ErrorCode = ErrorCode.NOT_FOUND_USERNAME
) : CustomException(errorCode.code, errorCode.message)