package com.example.fitnesapp

// Workout.kt
data class Workout(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int, // in minutes
    val difficulty: DifficultyLevel,
    val exercises: List<Exercise>
)

data class Exercise(
    val name: String,
    val description: String,
    val duration: Int, // in seconds
    val imageUrl: String?
)

enum class DifficultyLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}