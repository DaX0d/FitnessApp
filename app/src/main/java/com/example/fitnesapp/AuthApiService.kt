package com.example.fitnesapp

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(@Field("username") email: String,
                      @Field("password") password: String): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getProfile(): Response<ProfileResponse>

    companion object {
        fun create(baseUrl: String, context: Context): AuthApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor(context))
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApiService::class.java)
        }
    }
}

data class ProfileResponse(
    val username: String
)