package com.example.meritmatch

data class User (
    var username : String,
    var password : String,
    var login : Boolean,
    var karma_points : Double,
    val referral_code: String,
    val date : String,
    val avg_rating : Double
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
    val id : Int,
    val referral_code: String
)

data class Review (
    var description : String,
    var rating : Int,
    var poster_id : Int,
    var poster_name : String,
    var subject_id : Int,
    var task_id : Int,
    var post_time : String
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
    val payment : Boolean = false,
    val reviewed : Boolean = false
)

data class HealthStatus (
    val status : String
)

data class Tasks (
    val code: Int,
    val tasks : List<Task>
)

data class TaskIds (
    var code : Int,
    val task_list : List<Int>
)

data class Balance (
    val balance : Double,
    val code : Int
)