package com.example.authserver.mail

import org.springframework.stereotype.Service

@Service
interface EmailService {

    fun sendSimpMessage(to : String, subject : String, text : String)
}