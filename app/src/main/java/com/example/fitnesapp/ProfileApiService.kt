package com.example.fitnesapp

import retrofit2.Response
import retrofit2.http.GET

interface ProfileApiService {
    @GET("api/user/profile")
    suspend fun getProfile(): Response<UserProfile>
}