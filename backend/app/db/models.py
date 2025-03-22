from sqlalchemy import Column, Integer, String, Float
from .database import Base, engine

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    age = Column(Integer)
    weight = Column(Float)

Base.metadata.create_all(bind=engine)
