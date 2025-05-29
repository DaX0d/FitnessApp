package com.example.fitnesapp

import retrofit2.http.GET
import retrofit2.http.Path

interface WorkoutService {
    @GET("workouts")
    suspend fun getWorkouts(): List<Workout>

    @GET("workouts/{difficulty}")
    suspend fun getWorkoutsByDifficulty(
        @Path("difficulty") difficulty: DifficultyLevel
    ): List<Workout>
}

class WorkoutRepository(private val workoutService: WorkoutService) {
    suspend fun getAllWorkouts(): List<Workout> {
        return try {
            workoutService.getWorkouts()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWorkoutsByDifficulty(difficulty: DifficultyLevel): List<Workout> {
        return try {
            workoutService.getWorkoutsByDifficulty(difficulty)
        } catch (e: Exception) {
            emptyList()
        }
    }
}