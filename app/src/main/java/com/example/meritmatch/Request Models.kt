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
    val code : Int,
    val username : String,
    val karma_points: Double,
    val id : Int
)