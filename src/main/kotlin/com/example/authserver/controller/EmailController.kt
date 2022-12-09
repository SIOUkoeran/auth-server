package com.example.authserver.controller

import com.example.authserver.aop.ROLEChecker
import com.example.authserver.aop.RoleCheck
import com.example.authserver.dto.Response
import com.example.authserver.jwt.AuthToken
import com.example.authserver.mail.EmailService
import com.example.authserver.service.UserService
import org.jetbrains.annotations.NotNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/email")
class EmailController(
    private val emailsService : EmailService,
    private val userService: UserService
) {

    private val log = LoggerFactory.getLogger(EmailController::class.java)

    @PostMapping("/valid")
    suspend fun validEmailHandler(
        @RequestHeader("X-Authorization-email") email : String
    ) : Response{
        log.info("send valid email start : [${email}]")
        emailsService.sendIsValidEmail(email)
        return Response(
            code = 2000,
            message = "이메일 전송 완료되었습니다",
            data = null
        )
    }

    @PostMapping("/check")
    suspend fun checkEmailCode(
        @ROLEChecker("READY") role : String,
        @RequestBody requestCode: RequestCode,
        @RequestHeader("X-Authorization-email") email : String
    ) :Response {
        log.info("email code valid")
        emailsService.checkEmailCodeRedis(email, requestCode.code)
        val response = userService.changeRole(email, "USER")
        return Response(
            code = 2010,
            message = "이메일 인증이 완료되었습니다",
            data = response
        )
    }
}

data class RequestEmail(
    @NotNull
    val email : String,
)

data class RequestCode(
    @NotNull
    val code : String,
)