from fastapi import FastAPI

app = FastAPI()

@app.get('/')
def home():
    return {'messege': 'Hello, world!'}
