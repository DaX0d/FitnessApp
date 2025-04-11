package com.example.fitnesapp

// LoginRequest.kt
data class LoginRequest(
    val email: String,
    val password: String
)

// LoginResponse.kt
data class LoginResponse(
    val token: String,
    val userId: String,
    val expiresIn: Long
)

// ApiError.kt
data class ApiError(
    val message: String,
    val code: Int
)