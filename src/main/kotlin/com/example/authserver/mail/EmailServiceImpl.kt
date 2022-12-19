package com.example.authserver.mail

import com.example.authserver.exception.EmailSendException
import com.example.authserver.exception.NotMatchEmailCodeException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.coroutines.cancellation.CancellationException

@Component
class EmailServiceImpl(
    private val emailSender : JavaMailSender,
    private val redisEmailCodeStore: RedisEmailCodeStore,
) : EmailService{

    private val log = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    override suspend fun sendSimpMessage(to : String, subject : String, text : String) {
        try {
            val message = SimpleMailMessage()
            message.from = ("noreply@authserver.com")
            message.setTo(to)
            message.subject = subject
            message.text = text
            emailSender.send(message)
        }catch (e : Throwable) {
            throw CancellationException()
        }

    }


    override suspend fun sendEmailCodeRedis(key: String, value: String) {
        try {redisEmailCodeStore.push(key, value)}
        catch(e : Throwable) {
            throw CancellationException()
        }
    }

    @Transactional(readOnly = true)
    override suspend fun checkEmailCodeRedis(key: String, value : String) {
        val code = redisEmailCodeStore.getAwaitAndExpired(key)
        if (value != code)
            throw NotMatchEmailCodeException()
    }

    override suspend fun sendIsValidEmail(email: String): Unit = coroutineScope {
        val emailCode = UniqueCodeGenerator.generateUniqueCode(System.currentTimeMillis(), email)
        try{
            val emailSendFlag = async {
                sendSimpMessage(
                    email,
                    "auth-server-code",
                    emailCode
                )
                true
            }
            val redisFlag = async {
                sendEmailCodeRedis(email, emailCode)
                true
            }
            awaitAll(emailSendFlag, redisFlag)

        }catch (e : Throwable) {
            throw EmailSendException()
        }
    }
}