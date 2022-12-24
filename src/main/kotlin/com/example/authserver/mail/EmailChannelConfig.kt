package com.example.authserver.mail

import kotlinx.coroutines.channels.Channel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailChannelConfig {

    @Bean
    fun emailChannel() = Channel<EmailDto>()

}