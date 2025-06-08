package com.example.fitnesapp

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar_url") val avatarUrl: String
)
