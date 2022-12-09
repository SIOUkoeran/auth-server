package com.example.authserver

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AuthServerApplicationTests {

    @Test
    internal fun contextLoads() {
        val currentTimeMillis = System.currentTimeMillis()
        println(currentTimeMillis)
    }

}
