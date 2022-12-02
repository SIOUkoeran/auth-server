package com.example.authserver.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@EnableR2dbcRepositories
@Configuration
class PostgresConfig() {

    @Bean
    fun init(connectionFactory: ConnectionFactory) =
        ConnectionFactoryInitializer().apply{
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(
                ResourceDatabasePopulator(
                    ClassPathResource("scripts/schema.sql")
                )
            )
    }

    @Bean
    fun connectionFactory() : PostgresqlConnectionFactory{
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host("127.0.0.1")
                .database("ce")
                .username("postgres")
                .password("1234")
                .port(5432)
                .build()
        )
    }
}