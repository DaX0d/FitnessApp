from pydantic import BaseModel
from typing import Optional, List


class DifficultyCreate(BaseModel):
    name: str


class ExerciseCreate(BaseModel):
    name: str
    description: str
    duration: int
    image_url: Optional[str] = None


class WorkoutCreate(BaseModel):
    title: str
    description: str
    duration: int
    difficulty_id: int
    exercise_ids: List[int]


class ExerciseOut(BaseModel):
    id: int
    name: str
    description: str
    duration: int
    image_url: Optional[str]

    class Config:
        orm_mode = True


class WorkoutOut(BaseModel):
    id: int
    title: str
    description: str
    duration: int
    difficulty: str
    exercises: List[ExerciseOut]

    class Config:
        orm_mode = True
