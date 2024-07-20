from sqlalchemy import Integer, String, Column, Boolean, Double
from database import Base

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(50), unique=True, nullable=False)
    password = Column(String(60), nullable=False)
    karma_points = Column(Double)
    login = Column(Boolean)

class Post(Base):
    __tablename__ = 'posted_tasks'

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String(100))
    description = Column(String(5000))
    kp_value = Column(Double)
    user_id = Column(Integer)