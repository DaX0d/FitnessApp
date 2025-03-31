from pydantic import BaseModel, EmailStr, Field

class UserCreate(BaseModel):
    name: str = Field(max_length=20)
    email: EmailStr
    # age: int = Field(gt=0)
    # weight: float
    password: str

class UserLogin(BaseModel):
    name: str
    password: str
