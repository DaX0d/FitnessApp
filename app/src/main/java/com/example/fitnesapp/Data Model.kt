package com.example.fitnesapp

import com.google.gson.annotations.SerializedName

// LoginRequest.kt
data class LoginRequest(
    val email: String,
    val password: String
)

// LoginResponse.kt
data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)
// ApiError.kt
    data class ApiError(
    val message: String,
    val code: Int
)