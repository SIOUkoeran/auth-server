package com.example.authserver.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user")
class User(

    @Id
    val id : Long,

    @Column
    val username : String,

    @Column
    var password : String,

    @Column
    var email : String,

    @Column("created_at")
    val createAt : LocalDateTime,

    @Column("updated_at")
    var updatedAt : LocalDateTime

)

