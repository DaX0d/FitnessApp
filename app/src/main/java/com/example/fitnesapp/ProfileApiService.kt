package com.example.fitnesapp

import retrofit2.Response
import retrofit2.http.GET

interface ProfileApiService {
    @GET("me")
    suspend fun getProfile(): Response<UserProfile>
}