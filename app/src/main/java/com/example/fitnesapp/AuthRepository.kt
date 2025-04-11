package com.example.fitnesapp

import com.google.gson.Gson
import retrofit2.Response

class AuthRepository(private val apiService: AuthApiService) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
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

