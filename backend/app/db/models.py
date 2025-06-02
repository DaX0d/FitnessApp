from sqlalchemy import Column, Integer, String, Float, ForeignKey, Table
from sqlalchemy.orm import relationship

from .database import Base, engine


class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    email = Column(String, nullable=False)
    age = Column(Integer, nullable=True)
    weight = Column(Float, nullable=True)
    password_hash = Column(String, nullable=False)


workout_exercise_association = Table(
    'workout_exercise',
    Base.metadata,
    Column('workout_id', Integer, ForeignKey('workouts.id')),
    Column('exercise_id', Integer, ForeignKey('exercises.id'))
)


class DifficultyLevel(Base):
    __tablename__ = 'difficulty_levels'

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(20), unique=True, nullable=False)

    workouts = relationship("Workout", back_populates="difficulty_level")


class Workout(Base):
    __tablename__ = 'workouts'

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String(100), nullable=False)
    description = Column(String(500), nullable=False)
    duration = Column(Integer)

    difficulty_id = Column(Integer, ForeignKey('difficulty_levels.id'))
    difficulty_level = relationship("DifficultyLevel", back_populates="workouts")

    exercises = relationship("Exercise", secondary=workout_exercise_association, back_populates="workouts")


class Exercise(Base):
    __tablename__ = 'exercises'

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False)
    description = Column(String(500), nullable=False)
    duration = Column(Integer)
    image_url = Column(String, nullable=True)

    workouts = relationship("Workout", secondary=workout_exercise_association, back_populates="exercises")


# Base.metadata.drop_all(bind=engine)
# Exercise.__table__.drop(bind=engine)
# Workout.__table__.drop(bind=engine)
# DifficultyLevel.__table__.drop(bind=engine)
Base.metadata.create_all(bind=engine)
