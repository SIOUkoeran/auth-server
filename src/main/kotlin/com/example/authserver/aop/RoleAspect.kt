package com.example.authserver.aop


import com.example.authserver.exception.NotFoundRoleHeaderEXception
import com.example.authserver.filter.ReactiveRequestContextHolder.getRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Aspect
@Configuration
class RoleAspect() {
    private val log : Logger = LoggerFactory.getLogger(RoleAspect::class.java)

    @Pointcut("within(* com.example.controller..*)")
    fun apiSecurity(){}

    @Before("apiSecurity()")
    fun checkUserRole(joinPoint : ProceedingJoinPoint) {
        getRequest().mapNotNull {
            request -> request.headers.getFirst("X-Authorization-role")
        }.doOnError {
            throw NotFoundRoleHeaderEXception()
        }.doOnSuccess { it ->
            it.equals("User")
            joinPoint.proceed()
        }.subscribe()
    }
}