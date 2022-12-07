package com.example.authserver.config

import com.example.authserver.exception.InvalidTokenException
import com.example.authserver.jwt.AuthToken
import com.example.authserver.properties.WebProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
class WebConfig(
    private val webProperties: WebProperties
) : WebFluxConfigurer{

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(webProperties.originHost)
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .maxAge(3600)
    }
}

@Component
class AuthTokenResolver : HandlerMethodArgumentResolver{
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthToken::class.java)
    }

    private val log = LoggerFactory.getLogger(AuthTokenResolver::class.java)
    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        val authHeader = exchange.request.headers["Authorization"]?.first()
        log.info("${exchange.request.headers}")
        log.info(" ok : $authHeader")
        checkNotNull(authHeader)
        if (!authHeader.startsWith("Bearer"))
            throw InvalidTokenException()
        val token = authHeader.split(" ")[1]
        log.info("resolver pass")
        return token.toMono()
    }
}