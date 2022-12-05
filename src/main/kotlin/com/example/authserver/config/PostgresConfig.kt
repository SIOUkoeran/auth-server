package com.example.authserver.config

import com.example.authserver.properties.DBProperties
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
class PostgresConfig(
    private val dbProperties: DBProperties
) {

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
                .host(dbProperties.host)
                .database(dbProperties.database)
                .username(dbProperties.username)
                .password(dbProperties.password)
                .port(dbProperties.port)
                .build()
        )
    }
}