package com.example.fitnesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class DifficultySelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем зависимости
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.12:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WorkoutService::class.java)
        val repository = WorkoutRepository(service)
        val viewModelFactory = WorkoutViewModelFactory(repository)

        setContent {
            FitnessAppTheme {
                val viewModel: WorkoutViewModel = viewModel(factory = viewModelFactory)
                FitnessApp(viewModel = viewModel)
            }
        }
    }
}

