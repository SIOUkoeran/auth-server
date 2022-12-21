package com.example.authserver.service

import com.example.authserver.dto.RequestUpdateUser
import com.example.authserver.exception.InvalidRoleException
import com.example.authserver.exception.NotFoundUserException
import com.example.authserver.exception.NotFoundUserNameException
import com.example.authserver.model.User
import com.example.authserver.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

@Component
@Transactional
class AdminServiceImpl(
    private val userRepository: UserRepository
) : AdminService{

    private val log = LoggerFactory.getLogger(AdminServiceImpl::class.java)
    /**
     * take pageSize
     * drop pageSize * (page - 1)
     */
    @Transactional(readOnly = true)
    override suspend fun findAllUsers(page : Int, pageSize : Int): Pair<Int, Flow<User>> {
        val totalPage = (ceil(userRepository.count().toDouble()) / pageSize).toInt() + 1
        val users = userRepository.findAll().take(pageSize).drop((page - 1) * pageSize)
            .filter { it -> it.role != "ADMIN" }
        return totalPage to users
    }

    /**
     * user by userId modify function
     */
    override suspend fun modifyUser(userId: Long, requestUpdateUser: RequestUpdateUser) {
        val findUser: User = userRepository.findById(userId) ?: throw NotFoundUserNameException()
        with(requestUpdateUser) {
            if (findUser.role != requestUpdateUser.role)
                findUser.role = requestUpdateUser.role
            if (findUser.username != requestUpdateUser.username)
                findUser.username = requestUpdateUser.username
            else
                return@with
            userRepository.save(findUser)
        }
    }

    /**
     * admin service user delete by userId
     * user role must be user
     */
    override suspend fun deleteUser(userId: Long) {
        log.info("request delete user id $userId")
        with(userRepository.findById(userId) ?: throw NotFoundUserException()){
            if (role == "USER")
                userRepository.deleteById(userId)
            else
                throw InvalidRoleException()
        }
    }

}