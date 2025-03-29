from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.models import User
from app.db.database import get_db
from app.schemas.users import UserCreate

login_router = APIRouter()

@login_router.post('/users/')
def register_user(user: UserCreate, db: Session = Depends(get_db)):
    db.add(user)
    db.commit()
    db.refresh(user)
    return user
