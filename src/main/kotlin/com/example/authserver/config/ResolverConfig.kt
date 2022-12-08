package com.example.authserver.config

import com.example.authserver.aop.AuthTokenResolver
import com.example.authserver.aop.HandlerMethodResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Configuration
class ResolverConfig(
    private val handlerMethodResolver: HandlerMethodResolver,
    private val authTokenResolver: AuthTokenResolver
) : WebFluxConfigurer {


    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        super.configureArgumentResolvers(configurer)
        configurer.addCustomResolver(handlerMethodResolver)
        configurer.addCustomResolver(authTokenResolver)
    }
}