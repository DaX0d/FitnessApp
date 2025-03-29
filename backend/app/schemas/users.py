from pydantic import BaseModel, EmailStr, Field

class UserCreate(BaseModel):
    name: Field = Field(max_length=20)
    email: EmailStr
    age: Field = Field(gt=0)
    weight: float
