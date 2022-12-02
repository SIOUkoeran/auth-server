package com.example.authserver.bcrypt

import at.favre.lib.crypto.bcrypt.BCrypt

object BCryptUtils {
    fun bcrypt(input: String) =
        BCrypt.withDefaults().hashToString(12, input.toCharArray())

    fun verify(input: String, hashedInput: String): Boolean =
        BCrypt.verifyer().verify(input.toCharArray(), hashedInput).verified
}