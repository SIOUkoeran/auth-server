package com.example.authserver.redis

import com.example.authserver.mail.EmailDto
import com.example.authserver.mail.handler.EmailHandlerMap
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service


@Service
@EnableAsync
class RedisMailService(
    private val emailHandlerMap: EmailHandlerMap,
    private val mailRedisTemplate: ReactiveRedisTemplate<String, EmailDto>
) {

    private val log = LoggerFactory.getLogger(RedisMailService::class.java)
    /**
     * mail sub
     */
    suspend fun publish(channelTopic: String, message : EmailDto){
        mailRedisTemplate.convertAndSend(channelTopic, message).subscribe()
    }

    /**
     * mail channel sub
     */
    suspend fun subscribe(channelTopic : String) {
        mailRedisTemplate.listenTo(ChannelTopic.of(channelTopic))
            .map(ReactiveSubscription.Message<String, EmailDto>::getMessage)
            .subscribe{message ->
                log.info("subscribe $message type :  ${message.type}")
                val emailHandler = emailHandlerMap.getHandler(type = message.type)
                checkNotNull(emailHandler)
                emailHandler.send(message)
            }
    }
}