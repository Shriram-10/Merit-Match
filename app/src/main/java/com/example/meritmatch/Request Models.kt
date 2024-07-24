package com.example.meritmatch

data class User (
    var username : String,
    var password : String,
    var login : Boolean,
    var karma_points : Double
)

data class Code (
    val code : Int
)

data class Message (
    val code : String
)

data class LoginCode (
    var code : Int,
    val username : String,
    val karma_points: Double,
    val id : Int
)

data class Task (
    val id : Int,
    val title : String,
    val description : String,
    val username : String,
    val kp_value : Double,
    val user_id : Int,
    val post_time : String,
    val deadline : String,
    val reserved : Int,
    val completed : Boolean = false,
    val active : Boolean = true,
    val payment : Boolean = false
)

data class Tasks (
    val code: Int,
    val tasks : List<Task>
)

data class Balance (
    val balance : Double,
    val code : Int
)