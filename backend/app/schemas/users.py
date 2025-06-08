from pydantic import BaseModel, EmailStr, Field, AnyUrl


class UserCreate(BaseModel):
    name: str = Field(max_length=20)
    email: EmailStr
    password: str


class UserLogin(BaseModel):
    email: str
    password: str


class UserProfile(BaseModel):
    name: str
    email: EmailStr
    avatar_url: AnyUrl
