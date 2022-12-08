package com.example.authserver.controller

import com.example.authserver.aop.ROLEChecker
import com.example.authserver.aop.RoleCheck
import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.Response
import com.example.authserver.jwt.AuthToken
import com.example.authserver.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
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
        @AuthToken token: String,
        @ROLEChecker("USER") role: String,
    ): Response {
        return Response(
            code = 2000,
            message = "내 정보 불러오기 성공",
            data = userService.getUserByToken(token)!!
        )
    }
}