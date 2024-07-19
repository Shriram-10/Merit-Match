package com.example.meritmatch

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val baseUrl = "http://192.168.4.89:8000"

    data class stateOfUser (
        val loading : Boolean = false,
        val value : Int? = 0,
        val error : String? = null
    )

    data class UserAvailability(
        val loading : Boolean = false,
        val status : String? = null,
        val error : String? = null
    )

    fun createNewUser (username : String, password : String) {
        viewModelScope.launch {
            try {
                val response = dataService.createUser(User(username, password))
                _stateOfUserRetrieval.value = stateOfUser (
                    value = response.code,
                    loading = false
                )
            } catch (e : Exception) {
                _stateOfUserRetrieval.value = stateOfUser (
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

    private val _stateOfUserRetrieval = mutableStateOf(stateOfUser())
    val stateOfUserRetrieval : State<stateOfUser> = _stateOfUserRetrieval

    private val _stateOfCheckUsername = mutableStateOf(UserAvailability())
    val stateOfCheckUsername : State<UserAvailability> = _stateOfCheckUsername
}