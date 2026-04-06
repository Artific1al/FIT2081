package com.example.viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.ui.theme.ViewModelTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.GreetingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewModelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingScreen(innerPadding)
                }
            }
        }
    }
}


@Composable
fun GreetingScreen(
    innerPaddingValues: PaddingValues,
    viewModel: GreetingViewModel = viewModel()
) {
    val userName = viewModel.userName
    val greetingMessage = viewModel.greetingMessage
    val isLoading = viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = userName,
            onValueChange = { viewModel.updateUserName(it) },
            label = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.generateGreeting() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Generate Greeting")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = greetingMessage,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ViewModelTheme {
        Greeting("Android")
    }
}