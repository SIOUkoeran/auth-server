package com.example.authserver.mail

import com.example.authserver.exception.EmailSendException
import kotlinx.coroutines.*
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class ValidMailHandler(
    private val mailSender: JavaMailSender,
    private val redisEmailCodeStore: RedisEmailCodeStore
) : EmailHandler {



    override fun send(emailDto: EmailDto) {
        try{
            CoroutineScope(Dispatchers.Default).launch {
                sendSimpMessage(
                    emailDto,
                    mailSender
                )
            }
            CoroutineScope(Dispatchers.IO).launch {
                sendEmailCodeRedis(emailDto.to, emailDto.text)
            }

        }catch (e : Throwable) {
            throw EmailSendException()
        }
    }

    override fun getType(): String {
        return "VALID"
    }

    private suspend fun sendEmailCodeRedis(key: String, value: String) {
        try {redisEmailCodeStore.push(key, value)}
        catch(e : Throwable) {
            throw CancellationException()
        }
    }
}