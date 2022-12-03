package com.example.authserver.controller

import com.example.authserver.dto.RequestLogin
import com.example.authserver.service.UserService
import kotlinx.coroutines.reactor.mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
){
    private val log : Logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/signup")
    suspend fun signupHandler(
        @RequestBody requestLogin: RequestLogin
    ) = userService.signUp(requestLogin)

    @PostMapping("/signin")
    suspend fun signinHandler(
        @RequestBody requestLogin: RequestLogin
    ) = userService.signIn(requestLogin)

}