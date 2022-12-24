package com.example.authserver.mail

import com.example.authserver.exception.NotSupportEmailType
import org.springframework.context.ApplicationContext
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class EmailHandlerMap(
    private val applicationContext: ApplicationContext
) {

    private val emailHandlers : MutableMap<String, EmailHandler> =
        ConcurrentHashMap()

    fun registerHandler() {
        applicationContext.getBeansOfType(EmailHandler::class.java)
            .map { handler ->
                handler.value
            }
            .map { emailHandler ->
                emailHandlers[emailHandler.getType()] = emailHandler
            }
    }
    fun getHandler(type : String) : EmailHandler {
        return emailHandlers[type] ?: throw NotSupportEmailType()
    }

}