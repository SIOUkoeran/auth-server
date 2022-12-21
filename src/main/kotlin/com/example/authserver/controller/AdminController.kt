package com.example.authserver.controller

import com.example.authserver.aop.ROLEChecker
import com.example.authserver.aop.UserInfoChecker
import com.example.authserver.dto.RequestUpdateUser
import com.example.authserver.dto.Response
import com.example.authserver.model.User
import com.example.authserver.service.AdminService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService : AdminService
) {
    private val log = LoggerFactory.getLogger(AdminController::class.java)

    @GetMapping("/users")
    suspend fun findUserList(
        @ROLEChecker("ADMIN") role : String,
        @RequestParam("page") page : Int,
        @RequestParam("pageSize") pageSize: Int,
    ) :Response  {
        val (totalPage, users) = adminService.findAllUsers(page, pageSize)
        val response : Pair<Int, Any> = Pair(totalPage, users.toList())
        return Response(
            code = 2000,
            message = "USER_LIST_반환",
            data = response
        )
    }

    @PutMapping("/user")
    suspend fun modifyUserHandler(
        @RequestParam("userId") userId : Long,
        @ROLEChecker("ADMIN") role :String,
        @RequestBody requestUser: RequestUpdateUser
    ) : Response {
        log.info("request modify userId : $userId")
        adminService.modifyUser(userId, requestUser)
        log.info("success modify userId : $userId")
        return Response(
            code = 2010,
            message = "유저 변경 완료되었습니다",
            data = null
        )
    }

    @DeleteMapping("user")
    suspend fun deleteUserHanlder(
        @RequestParam("userId") userId : Long,
        @ROLEChecker("ADMIN") role : String,
    ) = Response(
            code = 2020,
            message = "유저 삭제가 완료되었습니다",
            data = adminService.deleteUser(userId)
        )
}