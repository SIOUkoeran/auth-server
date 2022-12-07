package com.example.authserver.exception

enum class ErrorCode(
    val code : Int,
    val message : String
) {
    //login
    EXIST_USER(409, "유저가 존재합니다."),
    NOT_FOUND_USERNAME(404, "유저 이름이 입력되지 않았습니다"),
    NOT_MATCH_PASSWORD(400, "패스워드가 일치하지 않습니다."),

    //jwt
    NOT_FOUND_TOKEN(404, "토큰이 존재하지 않습니다"),
    INVALID_TOKEN(401, "검증되지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다."),

    //header
    NOT_FOUND_ROLE(404, "헤더에서 role이 발견되지 않았습니다")
}