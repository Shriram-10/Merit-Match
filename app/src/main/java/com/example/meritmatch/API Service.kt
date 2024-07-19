package com.example.meritmatch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.4.89:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val dataService = retrofit.create(ApiService::class.java)

interface ApiService {
    @POST("/users")
    suspend fun createUser(@Body request: User) : Code

    @GET
    suspend fun checkUser(@Url fullUrl : String) : Message
}