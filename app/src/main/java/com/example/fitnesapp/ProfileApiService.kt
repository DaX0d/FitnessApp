package com.example.fitnesapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApiService {
    @GET("me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfile>
}