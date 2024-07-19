package com.example.meritmatch

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    data class stateOfUser (
        val loading : Boolean = false,
        val value : Int? = null,
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

    private val _stateOfUserRetrieval = mutableStateOf(stateOfUser())
    val stateOfUserRetrieval : State<stateOfUser> = _stateOfUserRetrieval
}