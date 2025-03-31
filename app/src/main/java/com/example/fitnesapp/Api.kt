package com.example.fitnesapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("sign_up")
    fun getData(): Call<List<String>> // Замените на нужный тип данных

    @POST("register")
    fun registerUser(@Body user: User): Call<RegisterResponse>
}
