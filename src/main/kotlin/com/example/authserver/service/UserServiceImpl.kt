package com.example.authserver.service

import com.example.authserver.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService{
    private val log : Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
}