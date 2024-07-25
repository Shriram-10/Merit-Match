package com.example.meritmatch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.80.89:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val dataService = retrofit.create(ApiService::class.java)

interface ApiService {
    @POST("/users")
    suspend fun createUser(@Body request: User) : Code

    @GET
    suspend fun checkUser(@Url fullUrl : String) : Message

    @POST
    suspend fun loginUser(@Url fullUrl : String, @Body request : User) : LoginCode

    @POST
    suspend fun logoutUser(@Url fullUrl: String) : Code

    @POST
    suspend fun postTask(@Url fullUrl: String, @Body request : Task) : Code

    @GET
    suspend fun availableTasks(@Url fullUrl : String) : Tasks

    @GET
    suspend fun submittedTasks(@Url fullUrl : String) : Tasks

    @GET
    suspend fun postedTasks(@Url fullUrl : String) : Tasks

    @GET
    suspend fun historyTasks(@Url fullUrl : String) : Tasks

    @GET
    suspend fun reservedTasks(@Url fullUrl : String) : Tasks

    @GET
    suspend fun waitingTasks(@Url fullUrl : String) : Tasks

    @POST
    suspend fun reserveTask(@Url fullUrl : String) : Code

    @POST
    suspend fun unreserveTask(@Url fullUrl: String) : Code

    @POST
    suspend fun modifyPost(@Url fullUrl: String, @Body request : Task) : Code

    @POST
    suspend fun deletePost(@Url fullUrl : String) : Code

    @POST
    suspend fun submitTask(@Url fullUrl: String) : Code

    @POST
    suspend fun unsubmitTask(@Url fullUrl: String) : Code

    @POST
    suspend fun approveSubmission(@Url fullUrl: String) : Code

    @POST
    suspend fun declineSubmission(@Url fullUrl : String) : Code

    @GET
    suspend fun getBalance(@Url fullUrl : String) : Balance
}