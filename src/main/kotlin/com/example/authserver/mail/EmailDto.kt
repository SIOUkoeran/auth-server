package com.example.authserver.mail

import com.fasterxml.jackson.annotation.JsonProperty


sealed class Mail(

)

data class EmailDto(
    @JsonProperty("to")
    val to : String,
    @JsonProperty("subject")
    val subject : String,
    @JsonProperty("text")
    val text: String,
    @JsonProperty("type")
    val type: String
) : Mail()
