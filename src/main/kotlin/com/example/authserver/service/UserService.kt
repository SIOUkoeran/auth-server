package com.example.authserver.service

import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.ResponseLogin
import org.springframework.stereotype.Service

@Service
interface UserService {

    suspend fun signUp(requestLogin : RequestLogin) : ResponseLogin
}