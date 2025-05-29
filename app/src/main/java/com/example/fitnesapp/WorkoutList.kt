package com.example.fitnesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutList(workouts: List<Workout>, onWorkoutSelected: (Workout) -> Unit) {
    LazyColumn {
        items(workouts) { workout ->
            WorkoutCard(workout = workout, onClick = { onWorkoutSelected(workout) })
        }
    }
}

@Composable
fun WorkoutCard(workout: Workout, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workout.title,
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = workout.description,
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Длительность: ${workout.duration} мин",
                    style = MaterialTheme.typography.body2
                )

                Chip(
                    label = when (workout.difficulty) {
                        DifficultyLevel.BEGINNER -> "Начинающий"
                        DifficultyLevel.INTERMEDIATE -> "Средний"
                        DifficultyLevel.ADVANCED -> "Продвинутый"
                    },
                    color = when (workout.difficulty) {
                        DifficultyLevel.BEGINNER -> Color.Green
                        DifficultyLevel.INTERMEDIATE -> Color.Yellow
                        DifficultyLevel.ADVANCED -> Color.Red
                    }
                )
            }
        }
    }
}

@Composable
fun Chip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}