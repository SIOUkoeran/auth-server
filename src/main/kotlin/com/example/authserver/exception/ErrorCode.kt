package com.example.authserver.exception

enum class ErrorCode(
    val code : Int,
    val message : String
) {
    EXIST_USER(409, "유저가 존재합니다.")
}