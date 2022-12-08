package com.example.authserver.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class User(

    @Id
    val id : Long? = null,

    @Column
    val username : String,

    @Column
    var password : String,

    @Column
    var email : String,

    @Column("created_at")
    @CreatedDate
    val createAt : LocalDateTime? = null,

    @Column("updated_at")
    @LastModifiedBy
    var updatedAt : LocalDateTime? = null,

    @Column("role")
    var role : String
)

