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

data class NotFoundTokenException(
    val errorCode: ErrorCode = ErrorCode.NOT_FOUND_TOKEN
) : CustomException(errorCode.code, errorCode.message)

data class InvalidTokenException(
    val errorCode: ErrorCode = ErrorCode.INVALID_TOKEN
) : CustomException(errorCode.code, errorCode.message)

data class JWTokenExpiredException(
    val errorCode : ErrorCode = ErrorCode.EXPIRED_TOKEN
) : CustomException(errorCode.code, errorCode.message)

data class NotFoundRoleHeaderException(
    val errorCode: ErrorCode = ErrorCode.NOT_FOUND_ROLE
) : CustomException(errorCode.code, errorCode.message)

data class InvalidRoleException(
    val errorCode: ErrorCode = ErrorCode.INVALID_ROLE
) : CustomException(errorCode.code, errorCode.message)

data class InvalidVertificationException(
    override val message : String
) : CustomException(401, message = message)