package com.example.reviewapp

//point is to review all the content from weeks 1-4
import com.example.reviewapp.R
//import android.R
import com.example.reviewapp.GreetingViewModel
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reviewapp.ui.theme.ReviewAppTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    data class Message(val text: String, val time: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReviewAppTheme {

                val navController: NavHostController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        //topbar that calls dropdown menu
                        topBar()
                    },
                    bottomBar = {
                        //generate the bottom bar


                        bottomNavBar(navController)
                        //bottomBar()
                    },
                    floatingActionButton = {
                        //floating action button
                        //floatingAction()

                    }
                ){innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(innerPadding)
                    ){
                        navHostExample(innerPadding, navController)

                        //moved to home screen

                     }

                }
            }
        }
    }
}


//useful link for getting stuff visualled layed out the way I intended:
//https://developer.android.com/develop/ui/compose/layouts/basics

//week 3 notes good refresher for ideas when doing A1 or prepping for interview
//https://edstem.org/au/courses/31394/lessons/100957/slides/712961


@Composable
fun floatingAction(){
    FloatingActionButton(onClick = {/*to do*/ }) {
        Icon(Icons.Filled.Add, contentDescription = "Add smth")
    }
}
@Composable
fun bottomBar() {
    //display a bottom bar for navigating to different pages

            BottomAppBar(
                modifier = Modifier.height(90.dp), //space by 60 pixels


                content = {
                    IconButton(onClick = {/*to do*/ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }

                    IconButton(onClick = {/*to do*/ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }

                    IconButton(onClick = {/*to do*/ }) {
                        Icon(Icons.Filled.Email, contentDescription = "Email Login")
                    }

                    /*add more Icon Buttons*/
                }
            )

}



//drop down menu by itself
@Composable
fun dropDownExample() {

    var expanded by remember { mutableStateOf(false) } //has the user clicked on the "3 dots"

    //Box to place a composable inside another composable
    Box(
        modifier = Modifier.padding(top = 20.dp),
        //contentAlignment = Alignment.TopEnd, - used when not in a actions subsection of a top bar


    ) {
        IconButton(onClick = { expanded = true }){
            Icon(Icons.Default.MoreVert, contentDescription = "display settings")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = -22.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Change colour settings") },
                onClick = { /* open up modal to select dark / light mode */ },
                leadingIcon = { Icon(Icons.Outlined.Favorite, contentDescription = null) }
            )
        }
    }
}


// display a dropdown menu you can click on to expand, then click off to make it disappear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(){

    //make topbar disappear when scroll down, and re-appear when scroll up
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,  //Color(red = 1f, green = 1f, blue = 1f, alpha = 1f),
                    titleContentColor = MaterialTheme.colorScheme.primary //Color(red = 1f, green = 1f, blue = 8f, alpha = 1f)
                ),
                title = {
                    Text(
                        "I love cheesecake btw",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },

                actions = {
                    dropDownExample()
                }
            )

}
// display top bar for current "settings" on given page -> useful for changing dark / light mode settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun modalExample(){

    var showDialog by remember { mutableStateOf(false)}
    var textFieldValue by remember { mutableStateOf("")}
    Button( onClick = { showDialog = true } ){
        Text( " Open Modal ")
    }

    if (showDialog){

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter Your Name") },
            text = {
                Column{
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if(textFieldValue.isNotEmpty()){
                        Text("Your name is $textFieldValue")
                    }
                }
            },

            confirmButton = {
                Button( onClick = {
                    showDialog = false

                }){
                    Text("Confirm")
                }
            },
            dismissButton =  {
                Button( onClick = { showDialog = false }){
                    Text("Cancel")
                }
            }

        )

    }
}
// display popup modals from a clicked button that provide more info about the current focus but block access to the rest
//of the screen until addressed


@Composable
fun sharedPrefExample(){

    var sliderValue by remember { mutableStateOf( 10f)}

    val con = LocalContext.current

    Button( onClick = {
        val sharedPref = con.getSharedPreferences("slider", Context.MODE_PRIVATE).edit()
        sharedPref.putFloat("val", sliderValue)
        sharedPref.apply()

    }){
        Text("Save Value")
    }

    Button( onClick = {
        val sharedPref = con.getSharedPreferences("slider", Context.MODE_PRIVATE)
        sliderValue = sharedPref.getFloat("val", 17f)


    }){
        Text("Load Value")
    }


    Spacer(Modifier.height(50.dp))

    Slider(
        value = sliderValue,
        onValueChange =  { sliderValue = it },
        valueRange = 0f..100f,

    )

    Text("Slider Value: $sliderValue")
    }

@Composable
fun cardExample(){

    OutlinedCard(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ){
        Text("Cheesecake is delicious")
        }

}


@Composable
fun lazyColumn(innerPadding: PaddingValues){

    var oneMessage by remember { mutableStateOf("")}
    var messageList by remember { mutableStateOf(listOf<MainActivity.Message>())}

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(20.dp) // large gap from topBar
    ){
        Text("Enter a delicious snack")

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = oneMessage,
            onValueChange = { oneMessage = it },
            label = { Text("Enter snack") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button( onClick = {
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            messageList = messageList + MainActivity.Message(oneMessage, currentTime)
            oneMessage = ""
        }) {
            Text("Add Message")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()) {


            items(messageList) { message ->

                //for each item
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ){
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Image(
                            painter = painterResource(R.drawable.omnom),
                            contentDescription = "ommy nom nommy",
                            modifier = Modifier.fillMaxWidth(0.6f)

                        )

                        Column {
                            //display text and time
                            Text(
                                "${message.text}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                            Text("${message.time}", modifier = Modifier.padding(10.dp))

                            IconButton(
                                onClick = {
                                    messageList = messageList.filter { it != message } //keep all other messages
                                },
                                modifier = Modifier.padding(10.dp)

                            ) {
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
//lazy column for displaying / adding medicines to be take one at a time


@Composable
fun HomeScreen(innerPadding: PaddingValues){
    Text("Home")
    //share intent
    //shareIntent(innerPadding)

    //mvvm
    var viewModel: GreetingViewModel = viewModel()
    mvvm(innerPadding, viewModel)

    //modal example
    //modalExample()

    //shared pref example
    //sharedPrefExample()

    //csv reading example
    //var count  = csvRead(LocalContext.current, "data.csv", "London")
    //Text("Count: $count")

    //card example
    //cardExample()

    //lazy column example
    //lazyColumn(innerPadding)


}

@Composable
fun LoginScreen(innerPadding: PaddingValues){
    Text("Login")
}

@Composable
fun SettingsScreen(innerPadding: PaddingValues){
    Text("Settings")
}

@Composable
fun bottomNavBar(navController: NavHostController){


    var selectedItem by remember { mutableStateOf(0)}

    val items = listOf(
        "home",
        "settings",
        "login"
    )

    NavigationBar{
        items.forEachIndexed { index, item ->

            NavigationBarItem(
            //icon
                icon = {
                    when (item) {
                        //if home
                        "home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "login" -> Icon(Icons.Filled.Lock, contentDescription = "Login")
                        "settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                label = { Text(item) },

                selected = (selectedItem == index),

                onClick = {
                    selectedItem = index
                    navController.navigate(item)
                }

            )
        }

    }
}


@Composable
fun navHostExample(innerPadding: PaddingValues, navController: NavHostController){

    NavHost(
        //use provided NavHostController
        navController = navController,
        //starting destination = home
        startDestination = "home"
    ){
        //home route
        composable("home"){
            HomeScreen(innerPadding)
        }

        //settings route
        composable("settings"){
            SettingsScreen(innerPadding)
        }

        //login route
        composable("login"){
            LoginScreen(innerPadding)
        }
    }


}
// using navhost and nav controller to navigate between pages


@Composable
fun shareIntent(paddingValues: PaddingValues) {

    var shareText by remember { mutableStateOf("") }

    Column() {
        Spacer(modifier = Modifier.height(20.dp))


        TextField(
            value = shareText,
            onValueChange = { shareText = it },
            label = { Text("Enter Text to share!") }
        )

        Spacer(modifier = Modifier.height(20.dp))
        val context = LocalContext.current

        Button(
            onClick = {
                val shareIntent = Intent(ACTION_SEND)

                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                context.startActivity(Intent.createChooser(shareIntent, "Share Text Via"))
            }) {
            Text("Share my text")
        }
    }
}



//sharing data with another app


@Composable
fun mvvm(innerPadding: PaddingValues,
         viewModel: GreetingViewModel){

    val userName = viewModel.userName
    val greetingMessage = viewModel.greetingMessage
    val isLoading = viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            value = userName,
            onValueChange = { viewModel.updateUserName(it)},
            label = { Text("Enter your name")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.generateGreeting()},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text("Generate Greeting")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if(isLoading){
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

    //model = data & repositories
    // view model = queries and logic that does NOT directly change view
    // view = the update to the view based on the output of the view model logic

    //i.e. model = database
    // view model = filter items in current memory and in database based on logic
    // view = update the screen to reflect the filtered list







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
    ReviewAppTheme {
        Greeting("Android")
    }
}



fun csvRead(context: Context, fileName: String, location: String): Int {
    var count = 0

    try {
        val inputStream = context.assets.open(fileName)

        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val values = line.split(",")

                if (values.size > 6 && values[7].trim() == location.trim()) {
                    count++
                }
            }
        }

    } catch (e: Exception) {
        return -23
    }
    return count
}
