package com.example.meritmatch

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val baseUrl = "http://192.168.48.89:8000"

    data class StateOfUser (
        val loading : Boolean = false,
        val value : Int? = 0,
        val error : String? = null
    )

    data class UserAvailability (
        val loading : Boolean = false,
        val status : String? = null,
        val error : String? = null
    )

    data class StateOfUserLogin (
        val loading : Boolean = false,
        val status : LoginCode = LoginCode(code = 0, username = "", karma_points = 0.0, id = 0),
        val error : String? = null
    )

    data class StateOfPost (
        val loading: Boolean = false,
        val status: Int = 0,
        val error: String? = null
    )

    data class StateOfGettingPosts (
        val loading: Boolean = false,
        val status: MutableState<List<Task>> = mutableStateOf<List<Task>>(listOf()),
        val error: String? = null
    )

    fun createNewUser (username : String, password : String, login: Boolean) {
        viewModelScope.launch {
            try {
                val response = dataService.createUser(User(username = username, password = password, login = login, karma_points = karma_points.value))
                _stateOfUserRetrieval.value = StateOfUser (
                    value = response.code,
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfUserRetrieval.value = StateOfUser (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun checkUser (username: String) {
        viewModelScope.launch {
            try {
                val response = dataService.checkUser("$baseUrl/users/$username")
                _stateOfCheckUsername.value = _stateOfCheckUsername.value.copy (
                    status = response.code,
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfCheckUsername.value = _stateOfCheckUsername.value.copy (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun loginUser (username: String, password: String, kp : Double) {
        viewModelScope.launch {
            try {
                val response = dataService.loginUser(
                    "$baseUrl/users/login/$username",
                    User (
                        username = username,
                        password = password,
                        karma_points = kp,
                        login = true
                    )
                )
                _stateOfLogin.value = _stateOfLogin.value.copy (
                    status = response,
                    loading = false
                )
                localUsername.value = response.username
                karma_points.value = response.karma_points
                user_id.value = response.id
            } catch (e : Exception) {
                _stateOfCheckUsername.value = _stateOfCheckUsername.value.copy (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun postTasks (userId: Int, post: Task) {
        viewModelScope.launch {
            try {
                val response = dataService.postTask(fullUrl = "$baseUrl/posts/create_post/$userId", post)
                _stateOfPost.value = _stateOfPost.value.copy (
                    status = response.code,
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfPost.value = _stateOfPost.value.copy (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun getAvailableTasks (userId: Int) {
        viewModelScope.launch {
            try {
                val response = dataService.availableTasks("$baseUrl/posts/get_all_tasks/$userId")
                _stateOfAllTasks.value = _stateOfAllTasks.value.copy (
                    status = mutableStateOf(response.tasks),
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfAllTasks.value = _stateOfAllTasks.value.copy (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun getReservedTasks (userId: Int) {
        viewModelScope.launch {
            try {
                val response = dataService.reservedTasks("$baseUrl/posts/get_reserved_tasks/$userId")
                _stateOfReservedTasks.value = _stateOfReservedTasks.value.copy (
                    status = mutableStateOf(response.tasks),
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfReservedTasks.value = _stateOfReservedTasks.value.copy (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun getPostedTasks (userId: Int) {
        viewModelScope.launch {
            try {
                val response = dataService.postedTasks("$baseUrl/posts/get_posted_tasks/$userId")
                _stateOfPostedTasks.value = _stateOfPostedTasks.value.copy (
                    status = mutableStateOf(response.tasks),
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfPostedTasks.value = _stateOfPostedTasks.value.copy (
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    private val _stateOfUserRetrieval = mutableStateOf(StateOfUser())
    val stateOfUserRetrieval : State<StateOfUser> = _stateOfUserRetrieval

    private val _stateOfCheckUsername = mutableStateOf(UserAvailability())
    val stateOfCheckUsername : State<UserAvailability> = _stateOfCheckUsername

    private val _stateOfLogin = mutableStateOf(StateOfUserLogin())
    val stateOfLogin : State<StateOfUserLogin> = _stateOfLogin

    private val _stateOfPost = mutableStateOf(StateOfPost())
    val stateOfPost : State<StateOfPost> = _stateOfPost

    private val _stateOfAllTasks = mutableStateOf(StateOfGettingPosts())
    val stateOfAllTasks : State<StateOfGettingPosts> = _stateOfAllTasks

    private val _stateOfReservedTasks = mutableStateOf(StateOfGettingPosts())
    val stateOfReservedTasks : State<StateOfGettingPosts> = _stateOfReservedTasks

    private val _stateOfPostedTasks = mutableStateOf(StateOfGettingPosts())
    val stateOfPostedTasks : State<StateOfGettingPosts> = _stateOfPostedTasks
}