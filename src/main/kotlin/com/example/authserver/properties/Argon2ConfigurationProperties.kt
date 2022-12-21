package com.example.authserver.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "argon2")
data class Argon2ConfigurationProperties(
    val iterations : Int,
    val memory : Int,
    val parallelism : Int
)
