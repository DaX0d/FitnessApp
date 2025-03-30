from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from .db.models import User
from .db.database import get_db
from .handlers import routers

app = FastAPI()

for router in routers:
    app.include_router(router)
