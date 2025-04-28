package com.example.fitnesapp

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class Authinterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = SecureTokenStorage.TokenStorage.getToken(context)

        return if (!token.isNullOrEmpty()) {
            chain.proceed(
                request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            )
        } else {
            chain.proceed(request)
        }
    }
}