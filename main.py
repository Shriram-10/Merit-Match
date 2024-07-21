from fastapi import FastAPI, Depends, status
from pydantic import BaseModel
from typing import Annotated
import models
from database import engine, SessionLocal
from sqlalchemy.orm import Session
from passlib.context import CryptContext
from datetime import datetime


app = FastAPI()
models.Base.metadata.create_all(bind=engine)

pwd_context = CryptContext(schemes=['bcrypt'], deprecated='auto')

def password_hash(password):
    return pwd_context.hash(password)

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

class PostBase(BaseModel):
    title: str
    description: str
    user_id: int
    post_time: str
    deadline: str
    kp_value: float
    reserved: int
    completed: bool = False


class User(BaseModel):
    username: str
    password: str
    karma_points: float
    login: bool = False


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


db_dependency = Annotated[Session, Depends(get_db)]


@app.post("/users", status_code=status.HTTP_201_CREATED)
async def create_user(user: User, db: db_dependency):
    new_user = models.User(**user.model_dump())
    existing_user = db.query(models.User).filter(models.User.username == new_user.username).first()

    if existing_user:
        return {"code" : -1}
    else:
       new_user.password = password_hash(user.username)
       new_user.login = user.login
       db.add(new_user)
       db.commit()
       return {"code" : 1} 


@app.get("/users/{username}")
async def username_availability(username: str, db: db_dependency):
    existing_user = db.query(models.User).filter(models.User.username == username).first()
    
    if existing_user is None:
        return {"code" : "available"}
    else:
        return {"code" : "unavailable"}


@app.post("/users/login/{username}")
async def login_auth(user: User, db: db_dependency):
    login_user = models.User(**user.model_dump())
    existing_user = db.query(models.User).filter(models.User.username == login_user.username).first()

    if existing_user:
        if verify_password(login_user.password, existing_user.password):
            setattr(existing_user, 'login', True)
            db.commit()
            return {
                "code" : 1,
                "username" : existing_user.username,
                "karma_points" : existing_user.karma_points,
                "id" : existing_user.id
            }
        else: 
            return {"code" : -2}
    else:
        return {"code" : -1}
    

@app.post("/create_post/{user_id}")
async def create_post(user_id: int, post: PostBase, db: db_dependency):
    new_post = models.Post(**post.model_dump())

    db.add(new_post)
    db.commit()

    return {"code" : 1}

@app.get("/get_posted_tasks/{user_id}")
async def get_sent_tasks(user_id: int, db: db_dependency):
    posted_tasks = db.query(models.Post).filter(user_id == models.Post.user_id).all()

    return {"tasks" : posted_tasks, "code" : 1}
    
@app.get("/get_reserved_tasks/{user_id}")
async def get_sent_tasks(user_id: int, db: db_dependency):
    reserved_tasks = db.query(models.Post).filter(user_id == models.Post.reserved).all()

    return {"tasks" : reserved_tasks, "code" : 1}

@app.get("/get_all_tasks/{user_id}")
async def get_all_tasks(user_id: int, db: db_dependency):
    all_tasks = db.query(models.Post).filter(user_id != models.Post.user_id).all()
    return {"tasks" : all_tasks, "code" : 1}

@app.post("/reserve_task/{user_id}/{task_id}")
async def reserve_task(user_id: int, task_id: int, db: db_dependency):
    reserved_task = db.query(models.Post).filter(models.Post.reserved == 0 and models.Post.id == task_id).first()

    if reserved_task:
        setattr(reserved_task, 'reserved', user_id)
        setattr(reserved_task, 'completed', False)
        db.comit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/send_notif/{user_id}/{task_id}")
async def send_notification(user_id: int, task_id: int, db: db_dependency):
    approved_task = db.query(models.Post).filter(models.Post.reserved > 0 and models.Post.user_id == user_id).first()

    if approved_task: 
        return {"Message" : "Task has been reserved by user {approved_task.username}"}
    else:
        return {"Message" : "error user"}

@app.post("/submit_task/{user_id}/{task_id}")
async def submit_task(task_id: int, user_id: int, db: db_dependency):
    get_task = db.query(models.Post).filter(models.Post.reserved == user_id and models.Post.id == task_id).first()

    if get_task:
        setattr(get_task, 'completed', True)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/acknowledge_submission/{user_id}/{task_id}")
async def accept_submission(task_id: int, user_id: int, db: db_dependency):
    view_task = db.query(models.Post).filter(task_id == models.Post.id and models.Post.user_id == user_id).first()

    if view_task:
        payer = db.query(models.User).filter(user_id == models.User.id).first()
        payee = db.query(models.User).filter(models.User.id == view_task.reserved).first()
        setattr(payer, 'karma_points', payer.karma_points - view_task.kp_value)
        setattr(payee, 'karma_points', payee.karma_points + view_task.kp_value)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/reject_submission/{user_id}/{task_id}")
async def refuse_submission(task_id: int, user_id: int, db: db_dependency):
    view_task = db.query(models.Post).filter(task_id == models.Post.id and models.Post.user_id == user_id).first()

    if view_task:
        setattr(view_task, 'completed', False)
        setattr(view_task, 'reserved', 0)
        setattr(view_task, 'post_time', datetime.now().__format__)
        return {"code" : 1}
    else:
        return {"code" : -1}