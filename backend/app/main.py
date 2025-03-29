from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from .db.models import User
from .db.database import get_db
from .handlers import routers

app = FastAPI()

for router in routers:
    app.include_router(router)

@app.get('/users/')
def get_user(db: Session = Depends(get_db)):
    return db.query(User).all()

@app.get('/', summary='Главная страница', tags=['Основные ручки'])
def get_home():
    return 'Hello world'
