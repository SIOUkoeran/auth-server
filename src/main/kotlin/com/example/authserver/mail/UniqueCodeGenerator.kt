package com.example.authserver.mail

import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import java.util.*

object UniqueCodeGenerator {

    private val encoder = Base64.getEncoder()
    private val log = LoggerFactory.getLogger(UniqueCodeGenerator::class.java)

    suspend fun generateUniqueCode(time : Long, email : String) = coroutineScope{
        val timeLast4 = time.toString().takeLast(4)
        val cutEmail = email.substringBefore("@")
        val encodedEmail = encoder.encodeToString(cutEmail.toByteArray())
        val code = timeLast4 + encodedEmail
        log.info("generate unique code : $code")
        code
    }
}