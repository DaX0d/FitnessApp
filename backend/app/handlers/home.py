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
ALGORITHM = "HS256"

# Инициализация схемы авторизации (OAuth2)
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")

home_router = APIRouter()


def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception

    user = db.query(User).filter(User.name == username).first()
    if user is None:
        raise credentials_exception
    return user


@home_router.get('/me/')
def get_me(current_user: User = Depends(get_current_user)):
    return {
        "id": current_user.id,
        "name": current_user.name,
        "email": current_user.email,
    }
