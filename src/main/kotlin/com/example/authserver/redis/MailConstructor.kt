package com.example.authserver.redis

import com.example.authserver.mail.EmailHandlerMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class MailConstructor(
    private val redisMailService: RedisMailService,
    private val emailHandlerMap: EmailHandlerMap
) : ApplicationRunner {
    private val log = LoggerFactory.getLogger(RedisMailService::class.java)
    private fun createMailRedisChannel() {
        GlobalScope.launch { redisMailService.subscribe("MAIL")}
    }
    private fun createMailHandlerMap() {
        emailHandlerMap.registerHandler()
    }
    override fun run(args: ApplicationArguments?) {
        log.info("subscribe request")
        createMailRedisChannel()
        createMailHandlerMap()
        log.info("subscribe success")
    }
}