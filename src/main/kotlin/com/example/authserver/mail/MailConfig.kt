package com.example.authserver.mail

import com.example.authserver.properties.MAILProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl


@Configuration
class MailConfig(
    private val mailProperties : MAILProperties
) {

    private val log = LoggerFactory.getLogger(MailConfig::class.java)
    @Bean
    fun getJavaMailSender() : JavaMailSender{
        val mailSender = JavaMailSenderImpl()
        with(mailProperties) {
            mailSender.host = host
            mailSender.port = port
            mailSender.username = username
            mailSender.password = password
        }
        val javaMailProperties = mailSender.javaMailProperties
        javaMailProperties["mail.transport.protocol"] = "smtp"
        javaMailProperties["mail.smtp.auth"] = "true"
        javaMailProperties["mail.smtp.starttls.enable"] = "true"
        javaMailProperties["mail.debug"] = "true"

        return mailSender

    }
}