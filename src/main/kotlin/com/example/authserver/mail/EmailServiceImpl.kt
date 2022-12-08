package com.example.authserver.mail

import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailServiceImpl(
    private val emailSender : JavaMailSender
) : EmailService{

    private val log = LoggerFactory.getLogger(EmailServiceImpl::class.java
    )
    override fun sendSimpMessage(to : String, subject : String, text : String) {
        val message = SimpleMailMessage()
        message.from = ("noreply@authserver.com")
        message.setTo(to)
        message.subject = subject
        message.text = text
        emailSender.send(message)
    }
}