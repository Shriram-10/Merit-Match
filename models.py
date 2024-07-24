from sqlalchemy import Integer, String, Column, Boolean, Double, DateTime, Interval
from database import Base
from datetime import datetime

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(50), unique=True, nullable=False)
    password = Column(String(255), nullable=False)
    karma_points = Column(Double)
    login = Column(Boolean)
    referral_code = Column(String(8))

class Post(Base):
    __tablename__ = 'posted_tasks'

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String(100))
    description = Column(String(5000))
    post_time = Column(String(20))
    deadline = Column(String(20))
    kp_value = Column(Double)
    user_id = Column(Integer)
    username = Column(String(50))
    reserved = Column(Integer)
    completed = Column(Boolean)
    payment = Column(Boolean)
    active = Column(Boolean)

class Review(Base):
    __tablename__ = 'reviews'

    id = Column(Integer, primary_key=True, index=True)
    poster_id = Column(Integer)
    subject_id = Column(Integer)
    description = Column(String(500))
    rating = Column(Double)
    task_id = Column(Integer)
    post_time = Column(String(20))