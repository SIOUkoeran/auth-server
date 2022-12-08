package com.example.authserver.controller

import com.example.authserver.dto.Response
import com.example.authserver.jwt.AuthToken
import com.example.authserver.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/jwt")
class JwtController(
    private val authService: AuthService
) {
    private val log = LoggerFactory.getLogger(JwtController::class.java)

    @PostMapping("/refresh")
    suspend fun createRefreshTokenHandler(
        @AuthToken token : String
    ) = Response(
            code = 2010,
            message = "access token 이 발급되었습니다",
            data = authService.refreshToken(token)
        )
}