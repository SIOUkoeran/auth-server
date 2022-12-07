package com.example.authserver.service

import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.ResponseLogin
import com.example.authserver.model.User
import com.example.authserver.redis.UserRedis
import com.example.authserver.redis.UserRedisDto
import org.springframework.stereotype.Service

@Service
interface UserService {

    suspend fun signUp(requestLogin : RequestLogin) : ResponseLogin
    suspend fun signIn(requestLogin : RequestLogin) : ResponseLogin
    suspend fun getToken(token : String) : UserRedis
    suspend fun getUserByToken(accessToken : String) : UserRedisDto?
}