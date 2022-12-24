package com.example.authserver.exception

enum class ErrorCode(
    val code : Int,
    val message : String
) {
    //login
    NOT_FOUND_USER(4000, "유저가 존재하지않습니다"),
    EXIST_USER(4090, "유저가 존재합니다."),
    NOT_FOUND_USERNAME(4040, "유저 이름이 입력되지 않았습니다"),
    NOT_MATCH_PASSWORD(4000, "패스워드가 일치하지 않습니다."),

    //jwt
    NOT_FOUND_TOKEN(4040, "토큰이 존재하지 않습니다"),
    INVALID_TOKEN(4010, "검증되지 않은 토큰입니다."),
    EXPIRED_TOKEN(4010, "토큰이 만료되었습니다."),

    //header
    NOT_FOUND_ROLE(4040, "헤더에서 role이 발견되지 않았습니다"),
    INVALID_ROLE(4010, "권한이 없습니다"),
    NOT_FOUND_EMAIL_HEADER(4040, "헤더에서 email이 발견되지 않았습니다"),
    NOT_FOUND_UID_HEADER(4040, "헤더에서 id가 발견되지 않았습니다"),

    /**
     * email
     */
    FAILED_SEND_EMAIL(5020, "이메일 전송에 실패했습니다"),
    NOT_MATCH_EMAILCODE(4010, "이메일 코드가 일치하지 않습니다"),
    NOT_SUPPORT_EMAIL_TYPE(4090, "지원하지않는 이메일 타입입니다"),

    ALREADY_STATE_ROLE(4090, "이미 권한이 중복입니다")
}