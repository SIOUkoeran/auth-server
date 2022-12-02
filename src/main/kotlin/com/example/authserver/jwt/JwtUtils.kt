package com.example.authserver.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.authserver.properties.JwtProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
object JwtUtils {

    private val log : Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    fun createToken(jwtClaim: JwtClaim, jwtProperties: JwtProperties) =
        with(jwtProperties){
            return@with JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(Date())
                .withExpiresAt(Date(Date().time + expired * 100))
                .withSubject(subject)
                .withClaim("email", jwtClaim.email)
                .withClaim("userId", jwtClaim.userId)
                .withClaim("username", jwtClaim.username)
                .sign(Algorithm.HMAC256(secret))
        }

    fun decodeToken(token : String, secret : String, issuer : String) : DecodedJWT {
        val algorithm = Algorithm.HMAC256(secret)
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
        return verifier.verify(token)
    }
}