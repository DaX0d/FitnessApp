package com.example.fitnesapp

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Если это запрос на обновление токена, пропускаем добавление заголовка
        if (originalRequest.url.encodedPath.contains("refresh")) {
            return chain.proceed(originalRequest)
        }

        // Получаем токен из SecureTokenStorage
        val accessToken = SecureTokenStorage.TokenStorage.getToken(context)
            ?: return chain.proceed(originalRequest)

        // Добавляем токен в заголовок Authorization
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}