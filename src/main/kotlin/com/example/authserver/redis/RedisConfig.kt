package com.example.authserver.redis


import com.example.authserver.properties.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties
) {


    @Bean
    fun reactiveRedisTemplate(factory : LettuceConnectionFactory) : ReactiveRedisTemplate<String, UserRedis> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer: Jackson2JsonRedisSerializer<UserRedis> =
            Jackson2JsonRedisSerializer<UserRedis>(
                UserRedis::class.java
            )
        val builder: RedisSerializationContextBuilder<String, UserRedis> =
            RedisSerializationContext.newSerializationContext<String, UserRedis>(keySerializer)
        val context: RedisSerializationContext<String, UserRedis> =
            builder.value(valueSerializer).build()
        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory =
        with(redisProperties){
            LettuceConnectionFactory(host, port)
        }
}