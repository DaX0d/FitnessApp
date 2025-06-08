from .login import login_router
from .admin import admin_router
from .exercises import exercises_router

routers = [
    login_router,
    admin_router,
    exercises_router
]
