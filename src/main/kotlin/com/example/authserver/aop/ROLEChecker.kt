package com.example.authserver.aop

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ROLEChecker(
    public val role : String
)
