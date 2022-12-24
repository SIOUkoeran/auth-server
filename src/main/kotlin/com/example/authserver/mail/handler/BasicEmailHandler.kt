package com.example.authserver.mail.handler

import com.example.authserver.mail.EmailDto
import com.example.authserver.mail.EmailHandler
import kotlinx.coroutines.*
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class BasicEmailHandler(
    private val emailSender : JavaMailSender
) : EmailHandler {


    /**
     * basic email send
     */
    private val scope = CoroutineScope(Dispatchers.Default)
    override fun send(emailDto: EmailDto) {
        scope.launch { super.sendSimpMessage(emailDto ,emailSender) }
    }

    override fun getType(): String {
        return "BASIC"
    }

}