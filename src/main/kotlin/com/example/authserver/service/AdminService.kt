package com.example.authserver.service

import com.example.authserver.dto.RequestUpdateUser
import com.example.authserver.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
interface AdminService {
    suspend fun findAllUsers(page: Int, pageSize : Int) : Pair<Int, Flow<User>>
    suspend fun modifyUser(userId: Long, requestUpdateUser: RequestUpdateUser)
    suspend fun deleteUser(userId: Long)
}