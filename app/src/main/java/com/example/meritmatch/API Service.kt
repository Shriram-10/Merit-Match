package com.example.meritmatch

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

val client = OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.152.89:8000/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val dataService = retrofit.create(ApiService::class.java)

interface ApiService {

    @GET("health")
    suspend fun checkHealth(): Response<HealthStatus>

    @GET
    suspend fun getUserDetails(@Url fullUrl : String) : UserDetails

    @POST("/users")
    suspend fun createUser(@Body request: User) : Code

    @POST("/reviews/post_review")
    suspend fun postReview(@Body request: Review) : Code

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