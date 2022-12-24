package com.example.authserver.controller

import com.example.authserver.aop.ROLEChecker
import com.example.authserver.aop.UserInfo
import com.example.authserver.aop.UserInfoChecker
import com.example.authserver.dto.RequestAccount
import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.RequestPasswordChange
import com.example.authserver.dto.Response
import com.example.authserver.jwt.AuthToken
import com.example.authserver.mail.EmailDto
import com.example.authserver.mail.EmailHandler
import com.example.authserver.mail.EmailService
import com.example.authserver.mail.UniqueCodeGenerator
import com.example.authserver.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val emailService: EmailService,
) {
    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/signup")
    suspend fun signupHandler(
        @RequestBody requestLogin: RequestLogin
    ) = userService.signUp(requestLogin)

    @PostMapping("/signin")
    suspend fun signinHandler(
        @RequestBody requestLogin: RequestLogin,
    ) = userService.signIn(requestLogin)


    @GetMapping("/me")
    suspend fun getMeHandler(
        @UserInfoChecker info : UserInfo
    ): Response {
        log.info("${info.id}")
        return Response(
            code = 2000,
            message = "내 정보 불러오기 성공",
            data = userService.getUserById(info.id)!!
        )
    }

    @GetMapping("/help/pw")
    suspend fun findAccountHandler(
        @ROLEChecker("USER") role : String,
        @UserInfoChecker info : UserInfo
    ) :Response {
        log.info("info : $info")
        userService.findUserByEmail(info.email)
        emailService.sendIsValidEmail(info.email)
        return Response(
            code = 2000,
            message = "이메일 전송이 완료되었습니다.",
            data = null
        )
    }


    @PostMapping("/help/pw")
    suspend fun changeUserPasswordHandler(
        @RequestBody request : RequestPasswordChange,
        @UserInfoChecker info : UserInfo
    ) : Response{
        userService.changePassword(
            email = info.email,
            password = request.password,
        )
        return Response (
            code = 2000,
            message = "비밀번호가 변경되었습니다",
            data = null
        )
    }
}

