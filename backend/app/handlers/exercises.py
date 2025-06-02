from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.models import Workout, DifficultyLevel
from app.db.database import get_db
from app.schemas.exercises import WorkoutOut
from typing import List

exercises_router = APIRouter(prefix="/workouts", tags=["Workouts"])


@exercises_router.get("/", response_model=List[WorkoutOut])
def get_all_workouts(db: Session = Depends(get_db)):
    workouts = db.query(Workout).all()
    return [
        WorkoutOut(
            id=w.id,
            title=w.title,
            description=w.description,
            duration=w.duration,
            difficulty=w.difficulty_level.name,
            exercises=w.exercises
        ) for w in workouts
    ]


@exercises_router.get("/{difficulty}", response_model=List[WorkoutOut])
def get_workouts_by_difficulty(difficulty: str, db: Session = Depends(get_db)):
    difficulty_level = db.query(DifficultyLevel).filter(DifficultyLevel.name == difficulty.upper()).first()
    if not difficulty_level:
        raise HTTPException(status_code=404, detail="Difficulty level not found")

    workouts = db.query(Workout).filter(Workout.difficulty_id == difficulty_level.id).all()
    return [
        WorkoutOut(
            id=w.id,
            title=w.title,
            description=w.description,
            duration=w.duration,
            difficulty=w.difficulty_level.name,
            exercises=w.exercises
        ) for w in workouts
    ]
