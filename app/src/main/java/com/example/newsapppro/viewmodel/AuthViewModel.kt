package com.example.newsapppro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapppro.models.User
import com.example.newsapppro.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // LiveData for register result
    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    // LiveData for login result
    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult

    // Register user
    fun register(user: User) {
        viewModelScope.launch {
            try {
                repository.register(user)
                _registerResult.postValue(true)
            } catch (e: Exception) {
                _registerResult.postValue(false)
            }
        }
    }

    // Login user
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(username, password)
            _loginResult.postValue(user)
        }
    }
}
