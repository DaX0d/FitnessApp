from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from db.models import User
from db.database import SessionLocal
from schemas.users import UserCreate

login_router = APIRouter()

@login_router.post('/users/')
def register_user(user: UserCreate, db: Session = Depends(SessionLocal())):
    db.add(user)
    db.commit()
    db.refresh(user)
    return user
