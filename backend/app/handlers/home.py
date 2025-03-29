from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.models import User
from app.db.database import get_db
from app.schemas.users import UserCreate

home_router = APIRouter()

@home_router.get('/me')
def get_me():
    return 'Да да я'
