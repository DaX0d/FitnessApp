import os
from dotenv import load_dotenv

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from jose import JWTError, jwt
from fastapi.security import OAuth2PasswordBearer
from app.db.models import User
from app.db.database import get_db

load_dotenv()
# Параметры для JWT
SECRET_KEY = os.getenv('SECRET_KEY')
SECRET_KEY = 'KMSAFLJKNSFDKL'
ALGORITHM = "HS256"

# Инициализация схемы авторизации (OAuth2)
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/token")

home_router = APIRouter()


@home_router.get('/me/')
def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("name")
        if username is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid token payload",
                headers={"WWW-Authenticate": "Bearer"},
            )
    except JWTError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid token",
            headers={"WWW-Authenticate": "Bearer"},
        )

    user = db.query(User).filter(User.name == username).first()
    if user is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found",
        )

    return {
        "name": user.name,
        "email": user.email,
        # "age": user.age,
        # "weight": user.weight
    }
