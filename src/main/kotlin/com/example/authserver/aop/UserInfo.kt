package com.example.authserver.aop

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * UserInfoChecker annotation data class
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class UserInfo(
    val role : String,
    val email : String,
    val id : Long
)
