package com.example.authserver.redis


import com.example.authserver.exception.NotFoundTokenException
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.properties.JwtProperties
import com.example.authserver.repository.UserRepository
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.core.getAndAwait
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
@Transactional
class RedisTokenStore(
    private val userRedisTemplate: ReactiveRedisTemplate<String, UserRedis>,
    private val jwtProperties: JwtProperties,
    private val userRepository: UserRepository,
) {

    suspend fun awaitPush(token: String, user: UserRedis) {
        val operations: ReactiveValueOperations<String, UserRedis> = userRedisTemplate.opsForValue()
        operations.set(token, user).subscribe()
        userRedisTemplate.expire(token, Duration.ofSeconds(60L)).subscribe()
    }

    @Transactional(readOnly = true)
    suspend fun awaitGet(token: String): UserRedis? {
        val andAwait: UserRedis? = userRedisTemplate.opsForValue().getAndAwait(token)
            ?.let {
                return it
            }
        val decodeToken = JwtUtils.decodeToken(
            token = token,
            issuer = jwtProperties.issuer,
            secret = jwtProperties.secret
        )
        val email = decodeToken.claims["email"]!!.asString()
        return userRepository.findUserByEmail(email)?.let{ user ->
            val userRedis = UserRedis(
                id = user.id!!,
                email = user.email,
                username = user.username
            )
            awaitPush(
                token,
                userRedis
            )
            return@let userRedis
        }
    }


    suspend fun awaitDelete(token : String) =
        userRedisTemplate.opsForValue().delete(token)
            .onErrorMap {
                throw NotFoundTokenException()
            }.subscribe()
}