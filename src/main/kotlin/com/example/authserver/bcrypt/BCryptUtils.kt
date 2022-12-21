package com.example.authserver.bcrypt

import com.example.authserver.properties.Argon2ConfigurationProperties
import de.mkammerer.argon2.Argon2Factory
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class BCryptUtils(
    private val argon2ConfigurationProperties: Argon2ConfigurationProperties
) {

    suspend fun bcrypt(input: String)
        = coroutineScope{
            val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
            with(argon2ConfigurationProperties) {
                argon2.hash(
                    iterations,
                    memory,
                    parallelism,
                    input
                )
            }
        }


    suspend fun verify(input: String, hashedInput: String): Boolean = coroutineScope {
        val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
        argon2.verify(hashedInput, input)
    }
}