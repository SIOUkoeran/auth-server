package com.example.authserver.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "web")
data class WebProperties(
    val port : Int,
    val originHost : String
)