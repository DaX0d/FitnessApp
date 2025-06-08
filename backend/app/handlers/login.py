import bcrypt
from typing import Annotated
from datetime import timedelta

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.db.models import User
from app.db.database import get_db
from app.schemas.users import UserCreate, UserLogin, UserProfile
from app.utils.auth import create_access_token, decode_token, hashed_password, verify_password
from fastapi.security import OAuth2PasswordRequestForm, OAuth2PasswordBearer
from app.schemas.token import Token


ACCESS_TOKEN_EXPIRE_MINUTES = 15


login_router = APIRouter(prefix="/auth", tags=["Auth"])
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/login")


def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    try:
        payload = decode_token(token)
        email: str = payload.get("sub")
        user = db.query(User).filter(User.email == email).first()
        if not user:
            raise HTTPException(status_code=401, detail="User not found")
        return user
    except:
        raise HTTPException(status_code=401, detail="Invalid credentials")


@login_router.post('/sign_up')
def register_user(user: UserCreate, db: Session = Depends(get_db)):
    if db.query(User).filter(User.email == user.email).first():
        raise HTTPException(status_code=400, detail="Email already registered")
    user = User(
        name=user.name,
        email=user.email,
        password_hash=hashed_password(user.password).decode('utf-8')
    )
    db.add(user)
    db.commit()
    db.refresh(user)
    return {"msg": "Registration successful"}


@login_router.post('/login')
def login_user(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    user = db.query(User).filter(User.email == form_data.username).first()
    if not user or not verify_password(form_data.password, user.password_hash):
        raise HTTPException(status_code=401, detail="Invalid email or password")
    token = create_access_token(data={"sub": user.email})
    return {"access_token": token, "token_type": "bearer"}


@login_router.get("/me", response_model=UserProfile)
def get_profile(current_user: User = Depends(get_current_user)):
    return UserProfile(
        name= current_user.name,
        email= current_user.email,
        avatar_url= 'https://static.wikia.nocookie.net/joke-battles/images/2/26/Lion_%28The_Backrooms%29.jpg/revision/latest?cb=20250211015920'
    )
