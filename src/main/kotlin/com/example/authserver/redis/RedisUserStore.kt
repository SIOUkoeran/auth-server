package com.example.authserver.redis

import com.example.authserver.exception.NotFoundTokenException
import com.example.authserver.exception.NotFoundUserException
import com.example.authserver.exception.NotFoundUserNameException
import com.example.authserver.model.User
import com.example.authserver.repository.UserRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.core.getAndAwait
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono
import java.time.Duration

@Component
@Transactional
class RedisUserStore(
    @Qualifier(value = "userRedisTemplate")
    private val userRedisTemplate: ReactiveRedisTemplate<String, UserRedisDto>,
    private val userRepository: UserRepository,
) {
    private val log = LoggerFactory.getLogger(RedisUserStore::class.java)

    suspend fun getAwaitOrPut(id : String) : UserRedisDto {
         return userRedisTemplate.opsForValue().get(id)
             .switchIfEmpty(
                 userRepository.findById(id.toLong())
                     ?.let { user ->
                         log.info("redis push user ${user.id}")
                         val userRedisDto = UserRedisDto(user)
                         pushAwait(id, userRedisDto)
                         userRedisDto.toMono()
                     } ?: throw NotFoundUserException()
             ).awaitSingleOrNull() ?: throw NotFoundTokenException()
    }

    suspend fun pushAwait(id : String, user : UserRedisDto) {
        val operations: ReactiveValueOperations<String, UserRedisDto> = userRedisTemplate.opsForValue()
        operations.set(id, user).subscribe()
        userRedisTemplate.expire(id, Duration.ofMinutes(5L)).subscribe()
    }
}