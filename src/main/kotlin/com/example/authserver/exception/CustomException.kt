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

data class EmailSendException(
    val errorCode : ErrorCode = ErrorCode.FAILED_SEND_EMAIL
) : CustomException(errorCode.code, errorCode.message)

data class NotMatchEmailCodeException(
    val errorCode: ErrorCode = ErrorCode.NOT_MATCH_EMAILCODE
) : CustomException(errorCode.code, errorCode.message)

data class AlreadyStateRoleException(
    val errorCode : ErrorCode = ErrorCode.ALREADY_STATE_ROLE
) : CustomException(errorCode.code, errorCode.message)

data class NotFoundHeaderEmailException(
    val errorCode: ErrorCode = ErrorCode.NOT_FOUND_EMAIL_HEADER
) : CustomException(errorCode.code, errorCode.message)

data class NotFoundHeaderUserIdException(
    val errorCode: ErrorCode = ErrorCode.NOT_FOUND_UID_HEADER
) : CustomException(errorCode.code, errorCode.message)

data class NotFoundUserException(
    val errorCode: ErrorCode = ErrorCode.NOT_FOUND_USER
) : CustomException(errorCode.code, errorCode.message)


data class NotSupportEmailType(
    val errorCode: ErrorCode = ErrorCode.NOT_SUPPORT_EMAIL_TYPE
) : CustomException(errorCode.code, errorCode.message)