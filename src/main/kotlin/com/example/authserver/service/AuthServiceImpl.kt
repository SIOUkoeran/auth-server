package com.example.authserver.service

import com.example.authserver.jwt.JwtClaim
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.properties.JwtProperties
import com.example.authserver.redis.RedisTokenStore
import com.example.authserver.redis.UserRedis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AuthServiceImpl (
    private val jwtProperties: JwtProperties,
    private val tokenStore: RedisTokenStore
) : AuthService{
    private val log : Logger = LoggerFactory.getLogger(AuthServiceImpl::class.java)

    override suspend fun refreshToken(token : String) : String = coroutineScope {
        with(jwtProperties) {
            val claims = JwtUtils.decodeToken(token, issuer = issuer, secret = secret)
                .claims
            log.info("request for creating access token user Id : [${claims["userId"]!!.asString()}")
            val jwtClaim = JwtClaim(
                username = claims["username"]!!.asString(),
                userId = claims["userId"]!!.asLong(),
                email = claims["email"]!!.asString()
            )
            val userRedis = async {
                tokenStore.awaitPush(
                    token,
                    UserRedis(
                        username = claims["username"]!!.asString(),
                        id = claims["userId"]!!.asLong(),
                        email = claims["email"]!!.asString()
                    )
                )
            }
            JwtUtils.createToken(
                jwtClaim = jwtClaim,
                jwtProperties = this,
                type = "accessToken"
            )
        }
    }


}