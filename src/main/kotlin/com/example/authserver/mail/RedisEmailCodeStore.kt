package com.example.authserver.mail

import com.example.authserver.exception.NotFoundTokenException
import io.netty.util.Timeout
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
@Transactional
class RedisEmailCodeStore(
    @Qualifier("stringReactiveRedisTemplate")
    private val stringRedisTemplate : ReactiveRedisTemplate<String, String>
) {
    private val log = LoggerFactory.getLogger(RedisEmailCodeStore::class.java)

    suspend fun push(key : String, value : String) {
        log.info("email redis push key : [$key] value :  [$value]")
        stringRedisTemplate.opsForValue().set(key, value).subscribe()
        stringRedisTemplate.expire(key, Duration.ofMinutes(30L)).subscribe()
    }

    suspend fun getAwaitAndExpired(key : String) =
        stringRedisTemplate.opsForValue().getAndAwait(key)
            ?: throw NotFoundTokenException()

}