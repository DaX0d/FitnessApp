from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from .db.models import User
from .db.database import SessionLocal
from .handlers import routers

app = FastAPI()

for router in routers:
    app.include_router(router)

@app.get('/users/')
def get_user(db: Session = Depends(SessionLocal)):
    return db.query(User).all()
