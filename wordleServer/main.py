from fastapi import Depends, FastAPI, HTTPException
from sqlalchemy import func
from sqlalchemy.orm import Session

import database


database.Base.metadata.create_all(bind=database.engine)

app = FastAPI()


def get_db():
    db = database.SessionLocal()
    try:
        yield db
    finally:
        db.close()


@app.get("/wordle/draw")
def draw(db: Session = Depends(get_db)):
    answer = db.query(database.Word).order_by(func.rand()).limit(1).first()
    return answer.word


@app.get("/wordle/check")
def check(word: str, db: Session = Depends(get_db)):
    count = db.query(func.count()).filter(database.AllWords.word == word.upper()).scalar()
    return count > 0
