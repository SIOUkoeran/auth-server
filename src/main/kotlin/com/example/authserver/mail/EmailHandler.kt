package com.example.authserver.mail

import kotlinx.coroutines.CancellationException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
interface EmailHandler {

    /**
     * default sendSimpMessage
     */
    suspend fun sendSimpMessage(emailDto: EmailDto, emailSender : JavaMailSender) {
        try {
            val message = SimpleMailMessage()
            with(emailDto) {
                message.from = ("noreply@authserver.com")
                message.setTo(to)
                message.subject = subject
                message.text = text
                emailSender.send(message)
            }
        }catch (e : Throwable) {
            throw CancellationException()
        }
    }
    fun send(emailDto : EmailDto)
    fun getType() : String
}