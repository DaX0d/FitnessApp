package com.example.fitnesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class DifficultySelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем зависимости
        val retrofit = Retrofit.Builder()
            .baseUrl("ВАШ_BASE_URL")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WorkoutService::class.java)
        val repository = WorkoutRepository(service)
        val viewModel = WorkoutViewModel(repository)

        setContent {
            FitnessAppTheme {
                FitnessApp(viewModel = viewModel)
            }
        }
    }
}

