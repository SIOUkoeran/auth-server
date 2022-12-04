package com.example.authserver.service

import org.springframework.stereotype.Service

@Service
interface AuthService {

    suspend fun refreshToken(token : String) : String
}