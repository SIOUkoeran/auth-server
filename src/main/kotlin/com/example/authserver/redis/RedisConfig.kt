package com.example.authserver.redis


import com.example.authserver.mail.EmailDto
import com.example.authserver.properties.RedisProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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

    @Bean(name = ["stringReactiveRedisTemplate"])
    fun stringReactiveRedisTemplate(factory: LettuceConnectionFactory) : ReactiveRedisTemplate<String, String> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = StringRedisSerializer()
        val builder: RedisSerializationContextBuilder<String, String> =
            RedisSerializationContext.newSerializationContext<String, String>(keySerializer)
        val context: RedisSerializationContext<String, String> =
            builder.value(valueSerializer).build()
        return ReactiveRedisTemplate(factory, context)
    }

    @Bean(name = ["mailRedisTemplate"])
    fun mailRedisTemplate(factory: LettuceConnectionFactory) : ReactiveRedisTemplate<String, EmailDto> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = Jackson2JsonRedisSerializer(EmailDto::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, EmailDto>(keySerializer)
        val context = builder.value(valueSerializer).build()
        return ReactiveRedisTemplate(factory, context)
    }

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