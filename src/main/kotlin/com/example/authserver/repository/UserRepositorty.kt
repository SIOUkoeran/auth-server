package com.example.authserver.repository

import com.example.authserver.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CoroutineCrudRepository<User, Long>{
    suspend fun findOneByEmail(email : String) : User
    suspend fun existsUserByEmail(email : String)
}