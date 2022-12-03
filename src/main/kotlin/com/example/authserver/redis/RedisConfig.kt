package com.example.authserver.redis


import com.example.authserver.properties.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext

@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties
) {
    @Bean
    fun reactiveRedisTemplate(factory: LettuceConnectionFactory) : ReactiveRedisTemplate<String, String> {
        val serializer = Jackson2JsonRedisSerializer(String::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, String>()
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun redisConnectionFactory() : RedisConnectionFactory{
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port)
    }
}