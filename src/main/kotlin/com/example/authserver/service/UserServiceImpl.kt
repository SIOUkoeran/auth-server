package com.example.authserver.service

import com.example.authserver.bcrypt.BCryptUtils
import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.ResponseLogin
import com.example.authserver.exception.AlreadyExistUserException
import com.example.authserver.exception.NotFoundUserNameException
import com.example.authserver.exception.UserPasswordException
import com.example.authserver.jwt.JwtClaim
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.model.User
import com.example.authserver.properties.JwtProperties
import com.example.authserver.redis.RedisTokenStore
import com.example.authserver.redis.RedisUserStore
import com.example.authserver.redis.UserRedis
import com.example.authserver.redis.UserRedisDto
import com.example.authserver.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtProperties: JwtProperties,
    private val redisTokenStore: RedisTokenStore,
    private val redisUserStore: RedisUserStore,
) : UserService{
    private val log : Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override suspend fun signUp(requestLogin: RequestLogin) : ResponseLogin{
        log.info("request sign up")
        if (userRepository.findUserByEmail(requestLogin.email) != null) {
            log.error("already exists : [${requestLogin.email}]")
            throw AlreadyExistUserException()
        }
        val user = with(requestLogin){
            User (
                username = username ?: throw NotFoundUserNameException(),
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
            ?: throw AlreadyExistUserException()){
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
            redisTokenStore.awaitPush(
                refreshToken,
                UserRedis(
                    username = username,
                    id = id,
                    email = email
                )
            )
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

    override suspend fun getToken(token: String): UserRedis {
        val user = redisTokenStore.awaitGetOrPut(token)
        log.info("request user using token : [${user.id}]")
        return user
    }

    override suspend fun getUserByToken(accessToken: String): UserRedisDto? {
        val userId = JwtUtils.decodeToken(
            accessToken,
            issuer = jwtProperties.issuer,
            secret = jwtProperties.secret
        ).claims["userId"]!!.toString()
        log.info("request api me : ${userId}")
        return redisUserStore.getAwaitOrPut(userId)
    }


    suspend fun getUser(id : Long): User =
        userRepository.findById(id) ?: throw NotFoundUserNameException()

}