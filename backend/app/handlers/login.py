from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.models import User
from app.db.database import get_db
from app.schemas.users import UserCreate
import bcrypt

def hashed_password(password: str):
    password_b = password.encode('utf-8')
    salt = bcrypt.gensalt()
    return bcrypt.hashpw(password=password_b, salt=salt)

login_router = APIRouter()

@login_router.post('/sugn_up/')
def register_user(user: UserCreate, db: Session = Depends(get_db)):
    new_user = User()
    new_user.name = user.name
    new_user.email = user.email
    new_user.age = user.age
    new_user.weight = user.weight
    new_user.password_hash = hashed_password(user.password).decode('utf-8')
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return {'message': 'success', 'user': new_user}

@login_router.post('/login/')
def login_user(name: str, password: str, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.name == name).first()
    if not user:
        return {'message': 'fail'}
    if bcrypt.checkpw(password.encode('utf-8'), user.password_hash.encode('utf-8')):
        return {'message': 'success'}
    return {'message': 'fail'}
