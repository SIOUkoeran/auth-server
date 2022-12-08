package com.example.authserver.filter

import org.springframework.http.server.reactive.ServerHttpRequest
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono


object ReactiveRequestContextHolder {

    val CONTEXT_KEY: Class<ServerHttpRequest> = ServerHttpRequest::class.java

    fun getRequest() : Mono<ServerHttpRequest> {
        return Mono.deferContextual{
            it -> val serverHttpRequest: ServerHttpRequest = it[CONTEXT_KEY]
            serverHttpRequest.toMono()
        }
    }

}