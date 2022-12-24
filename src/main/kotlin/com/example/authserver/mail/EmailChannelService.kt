package com.example.authserver.mail


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailChannelService(
    private val emailChannel : Channel<EmailDto>,
    private val emailHandlers : HashMap<String, EmailHandler>
) {

    private val log = LoggerFactory.getLogger(EmailChannelService::class.java)

    /**
     * send emailChannel
     */
    suspend fun sendEmailChannel(emailDto: EmailDto) = coroutineScope{
        log.info("$emailDto")
        emailChannel.send(emailDto)
    }

    /**
     * receive emailChannel
     */
    suspend fun receiveEmailChannel() = coroutineScope {
        launch {
            for (email in emailChannel) {
                val handler = emailHandlers[email.type]
                checkNotNull(handler)
                handler.send(email)
            }
        }
    }
}