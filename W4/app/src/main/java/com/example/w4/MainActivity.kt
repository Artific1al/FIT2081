package com.example.w4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.w4.ui.theme.W4Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    data class Message(val text: String, val time: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            W4Theme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //LazyColumnScreen(innerPadding)
                    navFunction(rememberNavController())
                //}
            }
        }
    }
}


@Composable
fun navFunction(navController: NavHostController){

    //Initialise NavHostController for managing navigation
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MyBottomAppBar(navController)
        }
    ){ innerPadding ->
        //
        Column(modifier = Modifier.padding(innerPadding)){
            MyNavHost(innerPadding, navController)
        }
    }

}

@Composable
fun MyNavHost(innerPadding: PaddingValues, navController: NavHostController){
    //NavHost to defined navigation graph??
    NavHost(
        //use provided navhostcontroller
        navController = navController,
        //set starting destination to home
        startDestination = "home"
    ){
        //define composable for home route?
        composable("home"){
            HomeScreen(innerPadding)
        }
        composable("reports"){
            ReportsScreen(innerPadding)
        }
        composable("settings"){
            SettingsScreen(innerPadding)
        }

    }
}

@Composable
fun MyBottomAppBar(navController: NavHostController) {
    //state to track currently selected items
    var selectedItem by remember { mutableStateOf(0)}

    val items = listOf(
        "home,"
        "reports",
        "settings"
    )
}
//display home screen
@Composable
fun HomeScreen(innerPadding: PaddingValues){}

//display reports screen
@Composable
fun ReportsScreen(innerPadding: PaddingValues) {}

//display settings screen
@Composable
fun SettingsScreen(innerPadding: PaddingValues) {}



@Composable
fun LazyColumnScreen(innerPadding: PaddingValues) {

    //text input
    var aMessage by remember { mutableStateOf("") }
    // var to hold message list
    var messages by remember { mutableStateOf(listOf<MainActivity.Message>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        //Text label
        Text("Enter Your Message")
        Spacer(modifier = Modifier.height(8.dp)) // spacing
        //takes input
        TextField(
            value = aMessage,
            //on change -> update value
            onValueChange = { aMessage = it },
            label = { Text("Enter Message") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            messages = messages + MainActivity.Message(aMessage, currentTime)
            aMessage = ""
        }) {
            // add caption to button
            Text("Add message")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            // iterate through list using items() method and lambda var message
            items(messages) { message ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // what is arow??
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // display an image?
                        Image(
                            painter = painterResource(R.drawable.m1),
                            contentDescription = "m1",
                            //set size to 60% of width of parent
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )
                        Column {
                            //display text
                            Text(
                                "${message.text}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                            //display time
                            Text("${message.time}", modifier = Modifier.padding(8.dp))
                            IconButton(
                                onClick = {
                                    messages = messages.filter { it != message }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                //display delete icon in red
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
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
    W4Theme {
        Greeting("Android")
    }
}