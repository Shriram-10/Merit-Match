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
    date = Column(String(10))
    referral_code = Column(String(8))
    rating_count = Column(Integer)
    avg_rating = Column(Double)

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
    reviewed = Column(Boolean)

class Review(Base):
    __tablename__ = 'reviews'

    id = Column(Integer, primary_key=True, index=True)
    poster_id = Column(Integer)
    poster_name = Column(String(50))
    subject_id = Column(Integer)
    description = Column(String(500))
    rating = Column(Integer)
    task_id = Column(Integer)
    post_time = Column(String(20))