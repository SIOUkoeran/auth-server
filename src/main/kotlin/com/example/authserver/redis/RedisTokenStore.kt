package com.example.authserver.redis


import com.example.authserver.exception.InvalidTokenException
import com.example.authserver.exception.NotFoundTokenException
import com.example.authserver.exception.NotFoundUserNameException
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.properties.JwtProperties
import com.example.authserver.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
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
        return reactiveRedisTemplate.opsForValue().getAndAwait(token)
    }

    suspend fun awaitGetOrPut(token: String): UserRedis {
        return reactiveRedisTemplate.opsForValue().getAndAwait(token)
            ?.let {
                log.info("user token cache exist")
                reactiveRedisTemplate.expire(token, Duration.ofSeconds(60L)).subscribe()
                return@let it
            } ?: kotlin.run {
            val decodeToken = JwtUtils.decodeToken(
                token = token,
                issuer = jwtProperties.issuer,
                secret = jwtProperties.secret
            )
            val email = decodeToken.claims["email"]?.asString() ?: throw InvalidTokenException()
            log.info("email : $email")
            return@run userRepository.findOneByEmail(email)?.let { user ->
                log.info("user token cache not exist : [${user.id}]")
                val userRedis = UserRedis(
                    id = user.id!!,
                    email = user.email,
                    username = user.username
                )
                awaitPush(token, userRedis)
                return@let userRedis
            } ?: throw NotFoundUserNameException()
        }
    }

    suspend fun awaitDelete(token : String) =
        reactiveRedisTemplate.opsForValue().delete(token)
            .onErrorMap {
                throw NotFoundTokenException()
            }.subscribe()
}