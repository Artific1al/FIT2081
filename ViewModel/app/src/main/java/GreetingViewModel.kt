package com.example.myapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

// GreetingViewModel: A ViewModel to manage UI state related to greeting messages.
class GreetingViewModel : ViewModel() {

    var userName by mutableStateOf("")
        private set

    var greetingMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun updateUserName(name: String) {
        userName = name
    }

    fun generateGreeting() {
        viewModelScope.launch {
            isLoading = true
            delay(1000)

            if (userName.isNotBlank()) {
                val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

                greetingMessage = when (currentTime) {
                    in 0..11 -> "Good Morning, $userName!"
                    in 12..17 -> "Good Afternoon, $userName!"
                    else -> "Good Evening, $userName!"
                }
            } else {
                greetingMessage = "Please enter your name."
            }

            isLoading = false
        }
    }
}