import bcrypt

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.db.models import User
from app.db.database import get_db
from app.schemas.users import UserCreate, UserLogin
from app.utils.token import create_access_token
from fastapi.security import OAuth2PasswordRequestForm


def hashed_password(password: str):
    password_b = password.encode('utf-8')
    salt = bcrypt.gensalt()
    return bcrypt.hashpw(password=password_b, salt=salt)


login_router = APIRouter()


@login_router.post('/sign_up/')
def register_user(user: UserCreate, db: Session = Depends(get_db)):
    new_user = User()
    new_user.name = user.name
    new_user.email = user.email
    # new_user.age = user.age
    # new_user.weight = user.weight
    
    new_user.password_hash = hashed_password(user.password).decode('utf-8')

    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    return {'message': 'success',
            # 'user': new_user
           }


@login_router.post('/login/')
def login_user(user_get: UserLogin, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.name == user_get.name).first()
    if not user:
        return {'message': 'fail',
                'details': 'No such person'
                }
    
    if bcrypt.checkpw(user_get.password.encode('utf-8'), user.password_hash.encode('utf-8')):
        token = create_access_token(data={'name': user.name})
        return token
    
    return {'message': 'fail',
            'details': 'Invalid password'
            }


@login_router.post('/token')
def login_for_access_token(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    user = db.query(User).filter(User.name == form_data.username).first()
    if not user or not bcrypt.checkpw(form_data.password.encode('utf-8'), user.password_hash.encode('utf-8')):
        raise HTTPException(status_code=400, detail="Incorrect username or password")

    access_token = create_access_token(data={"name": user.name})
    return {"access_token": access_token, "token_type": "bearer"}
