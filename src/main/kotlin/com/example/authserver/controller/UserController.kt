package com.example.authserver.controller

import com.example.authserver.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
){
    private val log : Logger = LoggerFactory.getLogger(UserController::class.java)
}