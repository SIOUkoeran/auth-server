package com.example.authserver.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "db")
data class DBProperties(
    val port: Int,
    val host: String,
    val database : String,
    val username : String,
    val password: String,
)
