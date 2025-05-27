import os
from dotenv import load_dotenv

import datetime as dt
from jose import jwt, JWTError
load_dotenv()
SECRET_KEY = os.getenv('SECRET_KEY')
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 60

def create_access_token(data: dict, expires_delta: dt.timedelta = None):
    to_encode = data.copy()
    expire = dt.datetime.now(dt.timezone.utc) + (expires_delta or dt.timedelta(minutes=15))
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt
