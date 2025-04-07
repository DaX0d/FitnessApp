package com.example.fitnesapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("sign_up")
    fun registerUser(@Body user: User): Call<RegisterResponse>
}
