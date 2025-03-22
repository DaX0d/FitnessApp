from pydantic import BaseModel

class UserCreate(BaseModel):
    name: str
    age: int
    weight: float
