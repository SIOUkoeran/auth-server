package com.example.authserver.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, )
data class Response(
    val code : Int,
    val message : String,
    val data : Any,
    val dataTime : LocalDateTime = LocalDateTime.now()
){

}

