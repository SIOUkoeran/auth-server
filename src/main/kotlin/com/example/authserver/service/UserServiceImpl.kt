package com.example.authserver.service

import com.example.authserver.bcrypt.BCryptUtils
import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.ResponseLogin
import com.example.authserver.exception.UserNotFoundException
import com.example.authserver.exception.UserPasswordException
import com.example.authserver.jwt.JwtClaim
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.model.User
import com.example.authserver.properties.JwtProperties
import com.example.authserver.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtProperties: JwtProperties
) : UserService{
    private val log : Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override suspend fun signUp(requestLogin: RequestLogin) : ResponseLogin{
        log.info("request sign up")
        if (userRepository.existsUserByEmail(requestLogin.email)) {
            throw UserNotFoundException()
        }
        val user = with(requestLogin){
            User (
                username = username,
                email = email,
                password =  BCryptUtils.bcrypt(password)
            )
        }
        val save = userRepository.save(user)
        log.info("request sign up success : [${save.id}]")
        return ResponseLogin(
            userId = save.id!!,
            username = save.username,
            email = save.email
        )
    }

    override suspend fun signIn(requestLogin: RequestLogin) : ResponseLogin {
        return with(userRepository.findUserByEmail(requestLogin.email)
            ?: throw UserNotFoundException()){
            log.info("request login userId : [$id]")
            val isVerified = BCryptUtils.verify(requestLogin.password, password)
            if (!isVerified)
                throw UserPasswordException()
            val jwtClaim = JwtClaim(
                username = username,
                email = email,
                userId = id!!
            )
            val createToken = JwtUtils.createToken(jwtClaim, jwtProperties, "accessToken")
            val refreshToken = JwtUtils.createToken(jwtClaim, jwtProperties, "refreshToken")
            log.info("success login user Id : [$id]")
            ResponseLogin(
                username = jwtClaim.username,
                email = jwtClaim.email,
                userId = jwtClaim.userId,
                accessToken = createToken,
                refreshToken = refreshToken
            )
        }
    }

}