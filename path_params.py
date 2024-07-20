from enum import Enum
from fastapi import FastAPI

food_app = FastAPI()

@food_app.get("/")
def base_route():
    return "Welcome home, go to /get-item route and enter food item to get started"

class FoodEnum(str, Enum):
    dairy = 'dairy'
    vegetables = 'veggies'
    fruits = 'fruits'
    non_veg = 'NV'

    @classmethod
    def _missing_(cls, value):
        for member in cls:
            if member.value == value.lower():
                return member.value
        return None
    
@food_app.get("/get-item/{item_name}")
def get_item(item_name: str):
    if item_name in FoodEnum:
        return {"Message" : "Item Available!"}
    
    return {"Message" : "Item Unavailable"}

    