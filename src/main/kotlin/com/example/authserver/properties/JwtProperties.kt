package com.example.authserver.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer : String,
    val expired : Long,
    val secret : String,
    val subject : String

) {

}