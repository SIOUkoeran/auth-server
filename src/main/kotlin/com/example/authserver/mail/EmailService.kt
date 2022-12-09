package com.example.authserver.mail

import org.springframework.stereotype.Service

@Service
interface EmailService {

    suspend fun sendSimpMessage(to : String, subject : String, text : String)
    suspend fun sendEmailCodeRedis(key : String, value : String)
    suspend fun checkEmailCodeRedis(email : String, code : String)
    suspend fun sendIsValidEmail(email: String): Unit

}