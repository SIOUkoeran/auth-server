package com.example.authserver.redis


import com.example.authserver.model.User
import com.example.authserver.properties.RedisProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

    @Bean(name = ["userRedisTemplate"])
    fun reactiveUserRedisTemplate(factory: LettuceConnectionFactory) : ReactiveRedisTemplate<String, UserRedisDto> {
        val keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper()
        val javaTimeModule = JavaTimeModule()
        objectMapper.registerModule(javaTimeModule)
        val valueSerializer: Jackson2JsonRedisSerializer<UserRedisDto> =
            Jackson2JsonRedisSerializer(
                objectMapper,
                UserRedisDto::class.java
            )
        val builder: RedisSerializationContextBuilder<String, UserRedisDto> =
            RedisSerializationContext.newSerializationContext(keySerializer)
        val context: RedisSerializationContext<String, UserRedisDto> =
            builder
                .value(valueSerializer)
                .build()

        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory =
        with(redisProperties){
            LettuceConnectionFactory(host, port)
        }
}