package com.example.authserver.redis

import com.example.authserver.exception.NotFoundUserNameException
import com.example.authserver.model.User
import com.example.authserver.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.core.getAndAwait
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
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
         return userRedisTemplate.opsForValue().getAndAwait(id)
             ?: kotlin.run{
                 val findUser: User = userRepository.findById(id.toLong())
                     ?: throw NotFoundUserNameException()
                 val userDto =  UserRedisDto(findUser)
                 log.info("datetime : ${userDto.createdAt}")
                 pushAwait(id, userDto)
                 userDto
             }
    }

    suspend fun pushAwait(id : String, user : UserRedisDto) {
        val operations: ReactiveValueOperations<String, UserRedisDto> = userRedisTemplate.opsForValue()
        operations.set(id, user).subscribe()
        userRedisTemplate.expire(id, Duration.ofMinutes(5L)).subscribe()
    }
}