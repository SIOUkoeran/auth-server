package com.example.authserver.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "mail")
data class MAILProperties (
    val host : String,
    val port : Int,
    val username : String,
    val password : String,
)