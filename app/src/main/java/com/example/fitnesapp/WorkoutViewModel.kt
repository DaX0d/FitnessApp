package com.example.fitnesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    private val _selectedDifficulty = MutableStateFlow<DifficultyLevel?>(null)
    val selectedDifficulty: StateFlow<DifficultyLevel?> = _selectedDifficulty

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun selectDifficulty(difficulty: DifficultyLevel) {
        _selectedDifficulty.value = difficulty
        loadWorkoutsByDifficulty(difficulty)
    }

    private fun loadWorkoutsByDifficulty(difficulty: DifficultyLevel) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getWorkoutsByDifficulty(difficulty)
            _workouts.value = result
            _isLoading.value = false
        }
    }

    fun loadAllWorkouts() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getAllWorkouts()
            _workouts.value = result
            _isLoading.value = false
        }
    }
}