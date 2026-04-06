package com.example.bottombar

import android.R.attr.navigationIcon
import android.R.attr.title
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottombar.ui.theme.BottomBarTheme
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottomBarTheme() {
                MainScreen() // gpt combined both tpo and bottom bar in one go
             }
        }

//            BottomBarTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
        }
    }



/* gpt combination of both top and bottom drop down menus*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Centered Top App Bar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    DropdownMenu()
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(60.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Check, contentDescription = "Check icon")
                }
                IconButton(onClick = {
                    context.startActivity(Intent(context, SecondActivity::class.java))
                }) {
                    Icon(Icons.Filled.Home, contentDescription = "Go Home")
                }
                Button(onClick = { }) {
                    Text("Click Me")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, contentDescription = "Add something")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "week 2 Lab.")
        }
    }
}

/* gpt combination of both top and bottom drop down menus*/


@Composable
fun DropdownMenu() {
    // expanded  properties controlls is visible or not
    // true = visible
    var expanded by remember { mutableStateOf(false) }
    //box composable is used to layer composables inside each other?

    Box(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = { expanded = true}) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            //onDismissRequest handles dismissal of menu
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
           DropdownMenuItem(
               text = { Text("Edit") },
               onClick = { /* Handle Settings */},
               leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null)}
           )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                onClick = { /*more settings*/},
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                trailingIcon = { Text("F11", textAlign = TextAlign.Center)}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarExample() {
    // create a state to control behaviour
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                //colours proprerty customises appearance
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
            ),
                //Title displayed in center of app bar
                title = {
                    Text(
                        "Ceneterd Top App Bar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "localized description"
                        )
                    }
                },
                actions = {
                    DropdownMenu()
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "week 2 Lab."
        )
    }
}
@Composable
fun MyBottomBar() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(60.dp),
                content = {

                    val context = LocalContext.current // get current "context"?

                    IconButton(onClick = { /* TODO: Add action */ }) {
                        Icon(Icons.Filled.Check, contentDescription = "Check icon")
                    }
                    IconButton(onClick = {
                        context.startActivity(Intent(context, SecondActivity::class.java))
                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "Go Home")
                    }
                    Button(onClick = { /* TODO: Add button action */ }) {
                        Text("Click Me")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Add FAB action */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add something")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hello, Bottom Bar!", fontSize = 24.sp)
        }
    }
}

//
//@Composable
//fun MyBottomBar() {
//    Scaffold(
//        bottomBar = {
//            BottomAppBar(
//                modifier = Modifier.height(60.dp),
//                content = {
//                    IconButton(onClick = {/* add action*/ }) {
//                        Icon(Icons.Filled.Check, contentDescription = "Check Icon")
//                    }
//                    IconButton(onClick = {/* add action*/ }) {
//                        Icon(Icons.Filled.Home, contentDescription = "Go Home")
//                    }
//                    Button(onClick = {/* add action*/ }) {
//                        Text("Click Me!")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier.padding(innerPadding).fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Hello, Bottom Bar!", fontSize = 24.sp)
//        }
//    }
//}

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
    BottomBarTheme {
        Greeting("Android")
    }
}