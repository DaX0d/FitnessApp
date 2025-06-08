package com.example.fitnesapp

import android.content.Context
import com.google.gson.Gson
import retrofit2.Response

class AuthRepository(
    private val apiService: AuthApiService,
    private val context: Context
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    // Сохраняем токен при успешном логине
                    SecureTokenStorage.TokenStorage.saveToken(context, loginResponse.accessToken)
                    Result.success(loginResponse)
                } ?: Result.failure(Exception("Пустой ответ от сервера"))
            } else {
                val error = parseError(response)
                Result.failure(Exception(error.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<ProfileResponse> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val error = parseError(response)
                Result.failure(Exception(error.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseError(response: Response<*>): ApiError {
        return try {
            val errorBody = response.errorBody()?.string()
            Gson().fromJson(errorBody, ApiError::class.java)
        } catch (e: Exception) {
            ApiError("Неизвестная ошибка", response.code())
        }
    }
}

