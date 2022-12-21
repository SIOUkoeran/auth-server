package com.example.authserver.service

import com.example.authserver.bcrypt.BCryptUtils
import com.example.authserver.dto.RequestLogin
import com.example.authserver.dto.ResponseCheckEmail
import com.example.authserver.dto.ResponseLogin
import com.example.authserver.exception.*
import com.example.authserver.jwt.JwtClaim
import com.example.authserver.jwt.JwtUtils
import com.example.authserver.model.User
import com.example.authserver.properties.JwtProperties
import com.example.authserver.redis.*
import com.example.authserver.repository.UserRepository
import kotlinx.coroutines.*
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
                username = username!!,
                email = email,
                password =  BCryptUtils.bcrypt(password),
                role = "READY"
            )

        }
        val save = userRepository.save(user)
        val tokenList = JwtClaim(
            username = save.username,
            email = save.email,
            role = save.role,
            userId = save.id!!
        ).let{
            createATKAndRTK(jwtClaim = it, jwtProperties)
        }
        log.info("request sign up success : [${save.id}]")
        return ResponseLogin(
            userId = save.id,
            username = save.username,
            email = save.email,
            role = save.role,
            accessToken = tokenList[0],
            refreshToken = tokenList[1]
        )
    }

    override suspend fun signIn(requestLogin: RequestLogin) : ResponseLogin {
        log.info(requestLogin.email)
        return with(userRepository.findUserByEmail(requestLogin.email)
            ?: throw NotFoundUserException()){
            log.info("request login userId : [$id]")
            val isVerified = BCryptUtils.verify(requestLogin.password, password)
            if (!isVerified)
                throw UserPasswordException()
            val jwtClaim = JwtClaim(
                username = username,
                email = email,
                userId = id!!,
                role = role
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
                refreshToken = refreshToken,
                role = role
            )
        }
    }

    override suspend fun getToken(token: String): UserRedis {
        val user = redisTokenStore.awaitGetOrPut(token)
        log.info("request user using token : [${user.id}]")
        return user
    }

    override suspend fun getUserById(userId: Long): UserRedisDto? {
        log.info("request api me : ${userId}")
        return redisUserStore.getAwaitOrPut(userId.toString())
    }

    override suspend fun changeRole(email: String, role : String) : ResponseCheckEmail {
        val user = userRepository.findUserByEmail(email) ?: throw NotFoundUserNameException()
        if (user.role == role)
            throw AlreadyStateRoleException()
        user.role = role
        userRepository.save(user)
        val jwtClaim = with(user) {
            JwtClaim(
                userId = id!!,
                username = username,
                email = email,
                role = role
            )
        }
        val tokenList = createATKAndRTK(jwtClaim, jwtProperties)
        redisTokenStore.awaitPush(tokenList[1], UserRedis(user.id!!, user.username, user.email))
        return ResponseCheckEmail(
            accessToken = tokenList[0],
            refreshToken = tokenList[1],
            userId = user.id
        )
    }

    override suspend fun findPasswordByEmail(email: String, username: String) {

    }

    override suspend fun findUserByEmail(email: String) {

    }

    override suspend fun changePassword(email: String, password : String) {
        val user =
            userRepository.findUserByEmail(email) ?: throw NotFoundUserNameException()
        user.password = BCryptUtils.bcrypt(password)
        userRepository.save(user)
    }

    private suspend fun createATKAndRTK(jwtClaim : JwtClaim, jwtProperties: JwtProperties) =
        coroutineScope {
            val atk = async { JwtUtils.createToken(jwtClaim, jwtProperties, "accessToken") }
            val rtk = async { JwtUtils.createToken(jwtClaim, jwtProperties, "refreshToken") }
            listOf(atk.await(), rtk.await())
        }
    suspend fun getUser(id : Long): User =
        userRepository.findById(id) ?: throw NotFoundUserNameException()

}