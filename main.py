from fastapi import FastAPI, Depends, status
from pydantic import BaseModel, TypeAdapter
from typing import Annotated
import models
from database import engine, SessionLocal
from sqlalchemy.orm import Session
from passlib.context import CryptContext
from datetime import datetime
import random
import string


app = FastAPI()
models.Base.metadata.create_all(bind=engine)

pwd_context = CryptContext(schemes=['bcrypt'], deprecated='auto')

def password_hash(password):
    return pwd_context.hash(password)

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def generate_referral_code(length=8):
    characters = string.ascii_uppercase + string.digits
    return ''.join(random.choice(characters) for _ in range(length))

class PostBase(BaseModel):
    title: str
    description: str
    user_id: int
    username: str
    post_time: str
    deadline: str
    kp_value: float
    reserved: int
    completed: bool = False
    payment: bool = False
    active: bool = True


class User(BaseModel):
    username: str
    password: str
    karma_points: float
    login: bool = False
    referral_code: str


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
    existing_user = db.query(models.User).filter((models.User.referral_code == new_user.referral_code) & (models.User.username != new_user.username)).first()
    new_user.karma_points = 300
    if existing_user:
       existing_user.karma_points += 350
       new_user.karma_points += 100

    new_user.password = password_hash(user.password)
    new_user.login = True
    new_user.referral_code = generate_referral_code()
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
                "id" : existing_user.id,
                "referral_code" : existing_user.referral_code
            }
        else: 
            return {"code" : -2}
    else:
        return {"code" : -1}
    

@app.post("/posts/create_post/{user_id}")
async def create_post(user_id: int, post: PostBase, db: db_dependency):
    new_post = models.Post(**post.model_dump())
    new_post.post_time = datetime.now().__format__('%Y-%m-%d %H:%M:%S')
    db.add(new_post)
    db.commit()

    return {"code" : 1}

@app.get("/posts/get_posted_tasks/{user_id}")
async def get_sent_tasks(user_id: int, db: db_dependency):
    posted_tasks = db.query(models.Post).filter((user_id == models.Post.user_id) & (models.Post.active == True) & (models.Post.completed == False)).all()

    return {"tasks" : posted_tasks, "code" : 1}

@app.get("/posts/get_submitted_tasks/{user_id}")
async def get_submitted_tasks(user_id: int, db: db_dependency):
    submitted_tasks = db.query(models.Post).filter((user_id == models.Post.reserved) & (models.Post.active == True) & (models.Post.completed == True) & (models.Post.payment == False)).all()

    return {"tasks" : submitted_tasks, "code" : 1}
    
@app.get("/posts/get_reserved_tasks/{user_id}")
async def get_reserved_tasks(user_id: int, db: db_dependency):
    reserved_tasks = db.query(models.Post).filter((user_id == models.Post.reserved) & (models.Post.completed == False)).all()

    return {"tasks" : reserved_tasks, "code" : 1}

@app.get("/posts/get_all_tasks/{user_id}")
async def get_all_tasks(user_id: int, db: db_dependency):
    all_tasks = db.query(models.Post).filter((user_id != models.Post.user_id) & (models.Post.reserved == 0)).all()
    return {"tasks" : all_tasks, "code" : 1}

@app.get("/posts/get_waiting_tasks/{user_id}")
async def get_waiting_tasks(user_id: int, db: db_dependency):
    waiting_tasks = db.query(models.Post).filter((True == models.Post.completed) & (models.Post.user_id == user_id) & (models.Post.payment == False) & (models.Post.active == True)).all()

    return {"tasks" : waiting_tasks, "code" : 1}

@app.post("/posts/reserve_task/{user_id}/{task_id}")
async def reserve_task(user_id: int, task_id: int, db: db_dependency):
    reserved_task = db.query(models.Post).filter((models.Post.user_id != user_id) & (models.Post.reserved == 0) & (models.Post.id == task_id)).first()

    if reserved_task:
        setattr(reserved_task, 'reserved', user_id)
        setattr(reserved_task, 'completed', False)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/posts/unreserve_task/{user_id}/{task_id}")
async def unreserve_task(user_id: int, task_id: int, db: db_dependency):
    reserved_task = db.query(models.Post).filter((models.Post.reserved == user_id) & (models.Post.id == task_id)).first()
    
    if reserved_task:
        setattr(reserved_task, 'reserved', 0)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.get("/posts/send_notif/{user_id}/{task_id}")
async def send_notification(user_id: int, task_id: int, db: db_dependency):
    approved_task = db.query(models.Post).filter((models.Post.reserved > 0) & (models.Post.user_id == user_id)).first()
    requested_user = db.query(models.User).filter(approved_task.reserved == models.User.id).first()
    if approved_task and requested_user: 
        return {"Message" : f"Task has been reserved by user {requested_user.id}"}
    else:
        return {"Message" : "error user"}

@app.post("/posts/submit_task/{user_id}/{task_id}")
async def submit_task(task_id: int, user_id: int, db: db_dependency):
    get_task = db.query(models.Post).filter((models.Post.reserved == user_id) & (models.Post.id == task_id)).first()

    if get_task:
        setattr(get_task, 'completed', True)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    

@app.post("/posts/unsubmit_task/{user_id}/{task_id}")
async def submit_task(task_id: int, user_id: int, db: db_dependency):
    get_task = db.query(models.Post).filter((models.Post.reserved == user_id) & (models.Post.id == task_id) & (models.Post.completed == True)).first()

    if get_task:
        setattr(get_task, 'completed', False)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/posts/acknowledge_submission/{user_id}/{task_id}")
async def accept_submission(task_id: int, user_id: int, db: db_dependency):
    view_task = db.query(models.Post).filter((task_id == models.Post.id) & (models.Post.user_id == user_id) & (models.Post.completed == True)).first()

    if view_task:
        payer = db.query(models.User).filter(user_id == models.User.id).first()
        payee = db.query(models.User).filter(models.User.id == view_task.reserved).first()
        if payer.karma_points >= view_task.kp_value:
            setattr(view_task, 'payment', True)
            setattr(payer, 'karma_points', payer.karma_points - view_task.kp_value)
            setattr(payee, 'karma_points', payee.karma_points + view_task.kp_value)
            db.commit()
            return {"code" : 1}
        else:
            return {"code" : -2}
    else:
        return {"code" : -1}
    
@app.post("/posts/reject_submission/{user_id}/{task_id}")
async def refuse_submission(task_id: int, user_id: int, db: db_dependency):
    view_task = db.query(models.Post).filter((task_id == models.Post.id) & (models.Post.user_id == user_id) & (models.Post.completed == True)).first()

    if view_task:
        setattr(view_task, 'completed', False)
        setattr(view_task, 'reserved', 0)
        setattr(view_task, 'post_time', datetime.now().__format__('%Y-%m-%d %H:%M:%S'))
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/users/logout/{username}")
async def logout_user(username : str, db : db_dependency):
    user = db.query(models.User).filter(username == models.User.username).first()

    if user:
        setattr(user,'login', False)
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.post("/posts/delete_post/{user_id}/{task_id}")
async def delete_post(user_id : int, task_id : int, db : db_dependency):
    post = db.query(models.Post).filter((user_id == models.Post.user_id) & (models.Post.id == task_id)).first()

    if post:
        setattr(post, 'reserved', 0)
        setattr(post, 'completed', False)
        setattr(post, 'active', False)
        db.commit()
        return {"code" : 1}
    else: 
        return {"code" : -1}
    
@app.post("/posts/modify_post/{user_id}/{task_id}")
async def modify_post(user_id : int, task_id : int, db : db_dependency, modified_post: PostBase):
    post = db.query(models.Post).filter((user_id == models.Post.user_id) & (models.Post.id == task_id)).first()

    if post:
        setattr(post, 'reserved', 0)
        setattr(post, 'completed', False)
        setattr(post, 'active', True)
        setattr(post, 'title', modified_post.title)
        setattr(post, 'description', modified_post.description)
        setattr(post, 'kp_value', modified_post.kp_value)
        setattr(post, 'deadline', modified_post.deadline)
        setattr(post, 'post_time', datetime.now().__format__('%Y-%m-%d %H:%M:%S'))
        db.commit()
        return {"code" : 1}
    else:
        return {"code" : -1}
    
@app.get("/users/get_balance/{user_id}")
async def get_balance(user_id: int, db: db_dependency):
    user = db.query(models.User).filter(models.User.id == user_id).first()
    if user:
        return {"balance" : user.karma_points, "code" : 1}
    else:
        return {"balance" : -1, "code" : -1}