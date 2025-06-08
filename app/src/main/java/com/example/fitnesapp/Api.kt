package com.example.fitnesapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/sign_up")
    fun registerUser(@Body user: User): Call<RegisterResponse>
}
