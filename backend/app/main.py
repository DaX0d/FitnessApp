from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from db.models import User
from db.database import SessionLocal
from handlers.login.login import router

app = FastAPI()
app.include_router(router)

@router.get('/users/')
def get_user(db: Session = Depends(SessionLocal)):
    return db.query(User).all()
