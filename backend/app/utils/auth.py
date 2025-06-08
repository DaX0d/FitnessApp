import os
from dotenv import load_dotenv
import datetime as dt

from jose import jwt, JWTError
import bcrypt

from app.schemas.token import Token, TokenData


load_dotenv(dotenv_path='../../.env')
SECRET_KEY = os.getenv('SECRET_KEY')
SECRET_KEY = '72696812ce6df768fe65b3f96b6c411091ea046e847e598a5a3ab6df19cc5e1d'

ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 60


def hashed_password(password: str):
    password_b = password.encode('utf-8')
    salt = bcrypt.gensalt()
    return bcrypt.hashpw(password=password_b, salt=salt)

def verify_password(plain, hashed) -> bool:
    return bcrypt.checkpw(plain.encode('utf-8'), hashed.encode('utf-8'))


def create_access_token(data: dict, expires_delta: dt.timedelta = None):
    to_encode = data.copy()
    expire = dt.datetime.now(dt.timezone.utc) + (expires_delta or dt.timedelta(minutes=15))
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)


def decode_token(token: str):
    return jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
