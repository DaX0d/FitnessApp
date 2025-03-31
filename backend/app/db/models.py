from sqlalchemy import Column, Integer, String, Float
from .database import Base, engine

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    email = Column(String, nullable=False)
    age = Column(Integer, nullable=True)
    weight = Column(Float, nullable=True)
    password_hash = Column(String, nullable=False)

Base.metadata.drop_all(bind=engine)
Base.metadata.create_all(bind=engine)
