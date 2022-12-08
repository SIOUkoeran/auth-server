package com.example.authserver.aop


import com.example.authserver.exception.InvalidTokenException
import com.example.authserver.exception.NotFoundRoleHeaderException
import com.example.authserver.filter.ReactiveRequestContextHolder.getRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import kotlin.jvm.Throws

@Component
@Aspect
class RoleAspect() {
    private val log : Logger = LoggerFactory.getLogger(RoleAspect::class.java)

    @Pointcut("@annotation(RoleCheck)")
    fun apiMeSecurity(){}

    @Before("apiMeSecurity()")
    @Throws(Throwable::class)
    @Order(1)
    fun checkUserRole(joinPoint : JoinPoint) {
        log.info("aop is working")
        val role = getRequest().mapNotNull { request ->
            request.headers.getFirst("X-Authorization-role")
            }
            .doOnError {
                throw NotFoundRoleHeaderException()
            }
        .doOnSuccess(){
            if (it == null || it != "USER")
                throw NotFoundRoleHeaderException()
        }.subscribe()
    }

    @AfterThrowing(pointcut = "apiMeSecurity()", throwing = "e")
    fun userRoleThrowing(e : Exception) {
        throw InvalidTokenException()
    }
}