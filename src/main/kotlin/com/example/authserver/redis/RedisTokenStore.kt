package com.example.authserver.redis


import com.example.authserver.exception.NotFoundTokenException
import com.example.authserver.exception.NotFoundUserNameException
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.properties.JwtProperties
import com.example.authserver.repository.UserRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono
import java.time.Duration

@Component
@Transactional
class RedisTokenStore(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, UserRedis>,
    private val jwtProperties: JwtProperties,
    private val userRepository: UserRepository,
) {

    private val log = LoggerFactory.getLogger(RedisTokenStore::class.java)
    suspend fun awaitPush(token: String, user: UserRedis) {
        reactiveRedisTemplate.opsForValue()
            .set(token, user, Duration.ofMinutes(100L))
            .subscribe { it ->
                if (!it)
                    throw NotFoundTokenException()
            }
    }

    suspend fun awaitGet(token : String) : UserRedis? {
        return reactiveRedisTemplate.opsForValue().get(token).awaitFirstOrNull()
    }

    suspend fun awaitGetOrPut(token: String): UserRedis {
        return reactiveRedisTemplate.opsForValue().get(token)
            .switchIfEmpty(
                userRepository.findOneByEmail(
                JwtUtils.decodeToken(
                    token,
                    jwtProperties.issuer,
                    jwtProperties.secret).claims["email"]!!.asString())
                    .let { user ->
                        val userRedis = UserRedis(
                            id = user.id!!,
                            email = user.email,
                            username = user.username
                        )
                        reactiveRedisTemplate.opsForValue()
                            .set(token, userRedis, Duration.ofSeconds(60L))
                        userRedis.toMono()
                    }).awaitFirstOrNull() ?: throw NotFoundUserNameException()

    }


    suspend fun awaitDelete(token : String) =
        reactiveRedisTemplate.opsForValue().delete(token)
            .onErrorMap {
                throw NotFoundTokenException()
            }.subscribe()
}