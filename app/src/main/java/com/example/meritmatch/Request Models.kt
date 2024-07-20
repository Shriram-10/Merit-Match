package com.example.meritmatch

data class User (
    var username : String,
    var password : String,
    var login : Boolean
)

data class Code (
    val code : Int
)

data class Message (
    val code : String
)