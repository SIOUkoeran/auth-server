package com.example.authserver.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user")
class User(

    @Id
    val id : Long? = null,

    @Column
    val username : String,

    @Column
    var password : String,

    @Column
    var email : String,

    @Column("created_at")
    val createAt : LocalDateTime? = null,

    @Column("updated_at")
    var updatedAt : LocalDateTime? = null
)

