from fastapi import FastAPI

app = FastAPI()


# TODO
@app.get("/wordle/draw")
async def draw():
    return "draw correct answer"


# TODO
@app.get("/wordle/check")
async def check(word: str):
    return "check answer"
