package com.example.authserver.service

import com.example.authserver.bcrypt.BCryptUtils
import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.ResponseLogin
import com.example.authserver.exception.UserExistException
import com.example.authserver.model.User
import com.example.authserver.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService{
    private val log : Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
    override suspend fun signUp(requestLogin: RequestLogin) : ResponseLogin{
        log.info("request sign up")
        if (userRepository.existsUserByEmail(requestLogin.email)) {
            throw UserExistException()
        }
        val user = with(requestLogin){
            User (
                username = username,
                email = email,
                password =  BCryptUtils.bcrypt(password)
            )
        }
        val save = userRepository.save(user)
        return ResponseLogin(
            userId = save.id!!,
            username = save.username,
            email = save.email
        )

    }

}