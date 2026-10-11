package com.example.medicare.model

data class User(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "PATIENT"
)
