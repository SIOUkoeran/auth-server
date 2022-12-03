package com.example.authserver.exception

enum class ErrorCode(
    val code : Int,
    val message : String
) {
    EXIST_USER(409, "유저가 존재합니다."),
    NOT_MATCH_PASSWORD(400, "패스워드가 일치하지 않습니다.")
}