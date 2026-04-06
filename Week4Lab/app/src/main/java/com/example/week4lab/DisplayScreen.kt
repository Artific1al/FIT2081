package com.example.week4lab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week4lab.ui.theme.Week4LabTheme

class DisplayScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week4LabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    displaySaved(innerPadding)

                    //show saved pref things here
                    /*Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }
    }
}


@Composable
fun displaySaved(padding: PaddingValues){
    val mcontext = LocalContext.current
    val shared = mcontext.getSharedPreferences("fruit", Context.MODE_PRIVATE)

    val name = shared.getString("name", "not found")
    val quantity = shared.getInt("quantity", 0)
    val ausGrown = shared.getBoolean("aus", false)

    Column(){

        Text("Fruit Name: ${name}")
        Spacer(Modifier.height(20.dp))

        Text("Fruit Quantity: ${quantity}")
        Spacer(Modifier.height(20.dp))

        Text("Aus Grown: ${ausGrown}")
        Spacer(Modifier.height(20.dp))



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
    Week4LabTheme {
        Greeting("Android")
    }
}