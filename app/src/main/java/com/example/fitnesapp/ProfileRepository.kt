package com.example.fitnesapp

import retrofit2.Response

class ProfileRepository(private val apiService: ProfileApiService) {
    suspend fun getUserProfile(): Response<UserProfile> {
        return apiService.getProfile()
    }
}