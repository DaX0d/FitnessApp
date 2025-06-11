package com.example.fitnesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun DifficultySelectionScreen(
    viewModel: WorkoutViewModel = viewModel(),
    onWorkoutSelected: (Workout) -> Unit = {}
) {
    val workouts by viewModel.workouts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Выберите уровень сложности",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        DifficultyButton(
            level = DifficultyLevel.BEGINNER,
            onClick = { viewModel.selectDifficulty(DifficultyLevel.BEGINNER) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DifficultyButton(
            level = DifficultyLevel.INTERMEDIATE,
            onClick = { viewModel.selectDifficulty(DifficultyLevel.INTERMEDIATE) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DifficultyButton(
            level = DifficultyLevel.ADVANCED,
            onClick = { viewModel.selectDifficulty(DifficultyLevel.ADVANCED) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            WorkoutList(workouts = workouts, onWorkoutSelected = onWorkoutSelected)
        }
    }
}

@Composable
fun DifficultyButton(level: DifficultyLevel, onClick: () -> Unit) {
    val buttonText = when (level) {
        DifficultyLevel.BEGINNER -> "Начинающий"
        DifficultyLevel.INTERMEDIATE -> "Средний"
        DifficultyLevel.ADVANCED -> "Продвинутый"
    }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = when (level) {
                DifficultyLevel.BEGINNER -> Color.Green
                DifficultyLevel.INTERMEDIATE -> Color.Blue
                DifficultyLevel.ADVANCED -> Color.Red
            }
        )
    ) {
        Text(text = buttonText, color = Color.White)
    }
}

@Composable
fun WorkoutDetailScreen(
    workout: Workout,
    onBack: () -> Unit,
    onStartWorkout: () -> Unit
) {
    // Implement your workout detail screen UI here
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = workout.title, style = MaterialTheme.typography.headlineMedium)
        Button(onClick = onStartWorkout) {
            Text("Start Workout")
        }
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    // Implement your error screen UI here
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)
        Button(onClick = onRetry) {
            Text("Retry")
        }
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun WorkoutExecutionScreen(
    workout: Workout,
    onFinish: () -> Unit
) {
    // Implement your workout execution screen UI here
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Executing: ${workout.title}")
        Button(onClick = onFinish) {
            Text("Finish Workout")
        }
    }
}

@Composable
fun FitnessApp(
    viewModel: WorkoutViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "difficulty_selection"
    ) {
        composable("difficulty_selection") {
            DifficultySelectionScreen(
                viewModel = viewModel,
                onWorkoutSelected = { workout ->
                    navController.navigate("workout_detail/${workout.id}")
                }
            )
        }

        composable(
            route = "workout_detail/{workoutId}",
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
            val workouts by viewModel.workouts.collectAsState()
            val workout = workouts.find { it.id == workoutId }

            if (workout != null) {
                WorkoutDetailScreen(
                    workout = workout,
                    onBack = { navController.popBackStack() },
                    onStartWorkout = {
                        navController.navigate("workout_execution/${workout.id}")
                    }
                )
            } else {
                ErrorScreen(
                    message = "Тренировка не найдена",
                    onRetry = { viewModel.loadAllWorkouts() },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "workout_execution/{workoutId}",
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
            val workouts by viewModel.workouts.collectAsState()
            val workout = workouts.find { it.id == workoutId }

            if (workout != null) {
                WorkoutExecutionScreen(
                    workout = workout,
                    onFinish = { navController.popBackStack("difficulty_selection", false) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    FitnessAppTheme {
        FitnessApp(viewModel = viewModel())
    }
}
