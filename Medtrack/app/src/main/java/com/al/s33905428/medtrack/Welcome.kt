package com.al.s33905428.medtrack

import android.content.Context
import androidx.compose.ui.graphics.Color
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme

/*


THERE WAS A BUG DURING THE INTERVIEW -> SOMETHING TO DO WITH LOADING MEDICAITONS INCORRECTLY?


 */

class Welcome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            val navController: NavHostController = rememberNavController()
            MedtrackTheme {
                Scaffold(/*modifier = Modifier.padding(top = 20.dp)inner padding */) { innerPadding ->
                    welcomeNavHost(navHostController = navController)
                    nothing(innerPadding)

                }
            }
        }
    }
}
@Composable
fun nothing(pad: PaddingValues){} // why?

@Composable
fun checkForExistingLogin(nav: NavHostController){

    val context = LocalContext.current

    val sharedPref =
        context.getSharedPreferences(
            "logged_in_patient_id",
            Context.MODE_PRIVATE
        )
    val ID = sharedPref.getString("ID", null)

    if (ID != null){
        nav.navigate("home")
    }


}

@Composable
fun welcomeScreen(nav: NavHostController){

    checkForExistingLogin(nav)

    Column(
        modifier = Modifier.padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(modifier = Modifier.fillMaxWidth()/*box to break out of column alignment*/){
        //how to make this on the top right?
        Text(
            text = "Allen Davies (33905428)",
            fontSize = (10.sp),
            modifier = Modifier.align(Alignment.TopEnd)
            //color = Color.GRAY,
            //fontWeight = FontWeight.Bold
            )

        }
        Spacer(Modifier.height(10.dp))
        //display app name
        Text("MedTrack", fontSize = 40.sp)
        //Spacer(Modifier.height(20.dp))

        //logo / header
        Image(
            painter = painterResource(R.drawable.icon),
            contentDescription = "Medical Icon",
            //modifier = Modifier.fillMaxWidth(0.5f)
        )

        Spacer(Modifier.height(40.dp))
        Text("Disclaimer: This app is foir tracking purposes only and does not replace professional" +
                "medical advice.", fontSize = (10.sp))

        Spacer(Modifier.height(40.dp))

        //link to monash health clinic website
        //how to pick colour? // underline?
        //https://developer.android.com/develop/ui/compose/quick-guides/content/support-multiple-links
        Text(
            text = buildAnnotatedString {
                append("Monash Health Clinic ")
                withLink(
                    LinkAnnotation.Url(
                        "https://monashhealth.org/contact/monash-medical-centre/",
                        TextLinkStyles(style= SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = Color.Cyan))
                    )
                ){
                    append("Website")

                }

            }
        )

        Spacer(Modifier.height(20.dp))

        Row(){

            Button( onClick = {
                    nav.navigate("login")
            }){
                Text("Login")
            }


        }





    }


}

@Composable
fun welcomeNavHost(navHostController: NavHostController){

    NavHost(
        navController = navHostController,
        startDestination = "welcome"

    ){
        composable("welcome"){
            welcomeScreen(navHostController)
        }

        composable("login"){
        loginScreen(navHostController)
        }

        composable("home"){
            homeScreen(navHostController)
        }

        composable("symptoms"){
            symptomsScreen(navHostController)
        }

        composable("signUp"){
            signUpScreen(navHostController)
        }

        composable("medication"){
            medScreen(navHostController)
        }

    }

}

@Composable
fun navBar(navController: NavHostController){


    var selectedItem by remember { mutableStateOf(0)}

    val items = listOf(
        "home",
        "symptoms",
        "medication"
    )

    NavigationBar{
        items.forEachIndexed { index, item ->

            NavigationBarItem(
                //icon
                icon = {
                    when (item) {
                        //if home
                        "home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "medication" -> Icon(Icons.Filled.AddCircle, contentDescription = "medication")
                        "symptoms" -> Icon(Icons.Filled.Medication, contentDescription = "symptoms")
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
