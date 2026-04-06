package com.example.reviewapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reviewapp.ui.theme.ReviewAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

//viewmodel to manage logic
class GreetingViewModel : ViewModel() {
    var userName by mutableStateOf("")
        private set // prevent external modification - can only
    //be updated via viewmodel functions

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