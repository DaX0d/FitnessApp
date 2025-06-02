from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from app.schemas.exercises import ExerciseCreate, WorkoutCreate, DifficultyCreate
from app.db.database import get_db
from app.db.models import DifficultyLevel, Workout, Exercise


admin_router = APIRouter(
    prefix="/admin",
    tags=["Admin"]
)


@admin_router.post("/difficulty", response_model=dict)
def add_difficulty(difficulty: DifficultyCreate, db: Session = Depends(get_db)):
    if db.query(DifficultyLevel).filter_by(name=difficulty.name).first():
        raise HTTPException(status_code=400, detail="Difficulty level already exists")

    new_difficulty = DifficultyLevel(name=difficulty.name)
    db.add(new_difficulty)
    db.commit()
    db.refresh(new_difficulty)
    return {"message": "Difficulty level added", "id": new_difficulty.id}


@admin_router.post("/exercise", response_model=dict)
def add_exercise(exercise: ExerciseCreate, db: Session = Depends(get_db)):
    new_exercise = Exercise(
        name=exercise.name,
        description=exercise.description,
        duration=exercise.duration,
        image_url=exercise.image_url
    )
    db.add(new_exercise)
    db.commit()
    db.refresh(new_exercise)
    return {"message": "Exercise added", "id": new_exercise.id}


@admin_router.post("/workout", response_model=dict)
def add_workout(workout: WorkoutCreate, db: Session = Depends(get_db)):
    if not db.query(DifficultyLevel).filter_by(id=workout.difficulty_id).first():
        raise HTTPException(status_code=404, detail="Difficulty level not found")

    # Получаем упражнения по ID
    exercises = db.query(Exercise).filter(Exercise.id.in_(workout.exercise_ids)).all()
    if len(exercises) != len(workout.exercise_ids):
        raise HTTPException(status_code=400, detail="One or more exercises not found")

    new_workout = Workout(
        title=workout.title,
        description=workout.description,
        duration=workout.duration,
        difficulty_id=workout.difficulty_id,
        exercises=exercises
    )

    db.add(new_workout)
    db.commit()
    db.refresh(new_workout)
    return {"message": "Workout added", "id": new_workout.id}
