package com.example.authserver.aop

import com.example.authserver.exception.InvalidRoleException
import com.example.authserver.exception.InvalidTokenException
import com.example.authserver.exception.NotFoundRoleHeaderException
import com.example.authserver.jwt.AuthToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class HandlerMethodResolver : HandlerMethodArgumentResolver{
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(ROLEChecker::class.java)

    }
    private val log = LoggerFactory.getLogger(HandlerMethodResolver::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> = mono{

        val role = async { getRoleValue(parameter) }
        val roleHeader = async { getHeader(exchange) }

        val roleAwait = role.await()
        if (roleAwait != roleHeader.await())
            throw InvalidRoleException()
        roleAwait
    }
    private suspend fun getRoleValue(parameter: MethodParameter) : String = coroutineScope <String>{
        parameter.getParameterAnnotation(ROLEChecker::class.java)?.role
            ?: throw NotFoundRoleHeaderException()
    }

    private suspend fun getHeader(exchange: ServerWebExchange) : String = coroutineScope{
        exchange.request.headers["X-Authorization-role"]?.first()
            ?: throw NotFoundRoleHeaderException()
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
        val authHeader: String? = exchange.request.headers["Authorization"]?.first()
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