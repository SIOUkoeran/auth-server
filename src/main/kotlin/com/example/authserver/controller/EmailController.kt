package com.example.authserver.controller

import com.example.authserver.aop.ROLEChecker
import com.example.authserver.aop.RoleCheck
import com.example.authserver.aop.UserInfo
import com.example.authserver.aop.UserInfoChecker
import com.example.authserver.dto.Response
import com.example.authserver.jwt.AuthToken
import com.example.authserver.mail.EmailDto
import com.example.authserver.mail.EmailService
import com.example.authserver.mail.UniqueCodeGenerator
import com.example.authserver.redis.RedisMailService
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
    private val emailService : EmailService,
    private val userService: UserService,
    private val redisMailService: RedisMailService,
) {

    private val log = LoggerFactory.getLogger(EmailController::class.java)

    @PostMapping("/valid")
    suspend fun validEmailHandler(
        @RequestHeader("X-Authorization-email") email : String
    ) : Response{
        log.info("send valid email start : [${email}]")
        redisMailService.publish(
            "MAIL",
            EmailDto(
                to = email,
                subject = "auth-code",
                text = UniqueCodeGenerator.generateUniqueCode(
                    System.currentTimeMillis(),
                    email
                ),
                type = "VALID"
            )
        )
        return Response(
            code = 2000,
            message = "이메일 전송 완료되었습니다",
            data = null
        )
    }

    @PostMapping("/check")
    suspend fun checkEmailCodeHandler(
        @RequestBody requestCode: RequestCode,
        @RequestHeader("X-Authorization-email") email : String
    ) :Response {
        emailService.checkEmailCodeRedis(email, requestCode.code)
        val response = userService.changeRole(email, "USER")
        return Response(
            code = 2010,
            message = "이메일 인증이 완료되었습니다",
            data = response
        )
    }

    @PostMapping("/help/check")
    suspend fun checkFindAccountCodeHandler(
        @ROLEChecker("USER") role : String,
        @UserInfoChecker info : UserInfo,
        @RequestParam("code") code : String,
    ) : Response {
        log.info("$code $info")
        emailService.checkEmailCodeRedis(
            email = info.email,
            code = code
        )
        return Response(
            code = 2000,
            message = "이메일 변경 코드가 확인되었습니다",
            data = null
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