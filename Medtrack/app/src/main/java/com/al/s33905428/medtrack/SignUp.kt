package com.al.s33905428.medtrack

import android.R
import android.R.attr.type
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.sequences.forEach


data class User(
    val ID : String,
    val name: String,
    val phoneNumber: String,
    val password: String
)

@Composable
fun signUpScreen(nav: NavHostController){

    //variables
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("")}
    var pass by remember { mutableStateOf("")}
    var confirmPass by remember { mutableStateOf("")}
    var nameErrorText by remember { mutableStateOf("") }
    var phoneErrorText by remember { mutableStateOf("")}
    var passErrorText by remember { mutableStateOf("    ")}
    var confirmErrorText by remember { mutableStateOf("")}
    var allValid by remember { mutableStateOf(true)}

    val spGet = LocalContext.current.getSharedPreferences("users", Context.MODE_PRIVATE)
    val spEdit = LocalContext.current.getSharedPreferences("users", Context.MODE_PRIVATE).edit()
    val gson = Gson()
    val context = LocalContext.current

    Column(){

        Text("Sign up", fontSize = 40.sp)


            Text("Name")
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your Name") }

            )
            Text("$nameErrorText", color = Color.Red,
                fontSize = 8.sp)
            Spacer(Modifier.height(10.dp))

        Text("Phone Number")
        TextField(
            value =  phone,
            onValueChange = { phone = it },
            label = { Text("Enter phone number")}
        )
        Text("$phoneErrorText", color = Color.Red,
            fontSize = 8.sp)
        Spacer(Modifier.height(10.dp))

        Text("Password")
        TextField(
            value =  pass,
            onValueChange = { pass = it },
            label = { Text("Enter a password")}
        )
        Text("$passErrorText", color = Color.Red,
            fontSize = 8.sp)
        Spacer(Modifier.height(10.dp))

        Text("Confirm Password")
        TextField(
            value =  confirmPass,
            onValueChange = { confirmPass = it },
            label = { Text("Re-enter your password")},

        )
        Text("$confirmErrorText", color = Color.Red,
            fontSize = 8.sp)
        Spacer(Modifier.height(10.dp))



        Row() {

            Button(
                onClick = {
                    nav.navigate("login") // not required but in case a user remembers they DO have an acc because of phone number or whatever
                }
            ){
                Text("Return to Login")
                //Plan -> get a "<-" icon button to put on this button

            }

            Box() {
                var sbHost = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()


                Button(
                    onClick = {

                        //reset all error texts so that only current errors are displayed
                            nameErrorText = ""
                            phoneErrorText = ""
                            passErrorText = ""
                            confirmErrorText = ""
                            allValid = true

                            //all fields are nonempty
                            if(name.isEmpty()) {
                                nameErrorText = "Name cannot be empty."
                                allValid= false
                            }

                            if(phone.isEmpty()){
                                phoneErrorText = "Phone number cannot be empty."
                                allValid= false}

                            if(pass.isEmpty()){
                                passErrorText = "Password cannot be empty."
                                allValid= false
                            }

                            if(confirmPass.isEmpty()){
                                confirmErrorText = "Confirmation password cannot be empty"
                                allValid= false
                            }

                            if (confirmPass.isNotEmpty() and pass.isNotEmpty() and (pass != confirmPass)){
                                allValid= false
                                if (confirmErrorText.isEmpty()){
                                    confirmErrorText = "Passwords do not match."
                                }
                                else {
                                    confirmErrorText += "\nPasswords do not match."
                                }
                            }

                            //if phone number not unique
                            if(!phoneUnique(phone, context, gson)){
                                allValid= false
                                if (phoneErrorText.isEmpty()){
                                    phoneErrorText = "Phone number already in system. Must be unqiue."
                                }
                                else{
                                    phoneErrorText += "\nPhone number already in system. Must be unqiue."
                                }
                            }

                            //if phone number doesnt start with 04 / is exactly 10
                            //digits

                           if((phone.length != 10 ) || (phone.length < 2)
                               || (phone.substring(0..1) != "04")){
                               allValid= false
                               if (phoneErrorText.isEmpty()){
                                   phoneErrorText = "Number must start with \"04\" and be 10 chars long"
                               }
                               else{
                                   phoneErrorText += "\nNumber must start with \"04\"and be 10 chars long"
                               }
                            }

                            //ensure password is at least 8 chars,contains at least one letter and one number (regex)

                            //(?=.* ) looks ahead to ensure at least one of the criteria specified in [] is found

                            val passPattern = Regex("(?=.*[a-zA-Z])(?=.*[0-9])")

                            if ((pass.length >= 8) and (!passPattern.containsMatchIn(pass))){
                                allValid= false
                                if (passErrorText.isEmpty()){
                                    passErrorText = "Password must be at least 8 chars and contain at least one letter and one number"
                                }
                                else{
                                    passErrorText +="\nPassword must be at least 8 chars and contain at least one letter and one number"
                                }
                            }

                            //all vailidation passed create new user
                            if (allValid) {

                                scope.launch() {

                                    //gen patient ID <_ come back to



                                    val patientID = getNextID(context, gson)

                                    var currentUser = User(
                                        ID = patientID,
                                        name = name,
                                        phoneNumber = phone,
                                        password = pass
                                    )

                                    //sample GSon to data class list example code found online and modified
                                    val usersGSON = spGet.getString("users", null)
                                    val type = object : TypeToken<MutableList<User>>() {}.type
                                    var usersList: MutableList<User> = mutableListOf()

                                    if (usersGSON != null){
                                        usersList = gson.fromJson(usersGSON, type)
                                    }
                                    usersList.add(currentUser)

                                    //now we have the list and can add it back to shared pref


                                    val gsonString = gson.toJson(usersList)
                                    spEdit.putString("users",gsonString)
                                    spEdit.apply()

                                    sbHost.showSnackbar("New user created!")
                                    nav.navigate("login")

                                }
                            }


                    }
                ) {
                    Text("Sign Up!")
                }


                SnackbarHost(hostState = sbHost)
            }
        }
    }
}

fun getNextID(context: Context, gson: Gson): String{
    //to be unique it cannot be in patients.csv
    //or in sharedPreferences

    val gson = gson
    val fileName = "patients.csv"
    val idColumn = 0
    val sp = context.getSharedPreferences("users", Context.MODE_PRIVATE)
    val usersGson = sp.getString("users", null)
    val type = object : TypeToken<MutableList<User>>() {}.type

    //vars
    var usersList: MutableList<User> = mutableListOf()
    var maxID = 0

    if (usersGson != null) {
        val usersList: MutableList<User> = gson.fromJson(usersGson, type)
    }

    // if at least one thing saved from
    if (usersList.isNotEmpty()){
        usersList.forEach { user ->
            val num = user.ID.substring(1).toInt() // remove the "P"
            if (num > maxID){
                maxID = num
            }
        }
    }

    try {

        //processing to find right column
        val inputStream = context.assets.open(fileName)
        var categoryIndex = 0

        val reader = BufferedReader(InputStreamReader(inputStream))
        val categories = reader.readLine().split(',')

        //find right value
        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")

                if (values[idColumn].substring(1).toInt() > maxID) {
                    maxID = values[idColumn].substring(1).toInt()

                }
            }
        }

    } catch (e: Exception) {
        return "P100000"
        Log.d("HIGHEST_ID", "Failed to open csv")
    }

    return "P${(maxID+1)}"

}

fun phoneUnique(phone: String, context: Context, gson: Gson): Boolean {

    val gson = gson
    val fileName = "patients.csv"
    val phoneColumn = 1
    val sp = context.getSharedPreferences("users", Context.MODE_PRIVATE)
    val usersGson = sp.getString("users", null)
    val type = object : TypeToken<MutableList<User>>() {}.type

    //vars
    var usersList: MutableList<User> = mutableListOf()

    if (usersGson != null) {
        val usersList: MutableList<User> = gson.fromJson(usersGson, type)
    }

    // if at least one thing saved from
    if (usersList.isNotEmpty()){
        Log.d("SHAREDPREF", "${usersList.get(0).phoneNumber}")
        usersList.forEach { user ->
            if (user.phoneNumber == phone){
                return false
            }
        }
    }

    //csv
    try {

        //processing to find right column
        val inputStream = context.assets.open(fileName)

        val reader = BufferedReader(InputStreamReader(inputStream))
        val categories = reader.readLine().split(',')

        //find right value
        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")

                if (values[phoneColumn] == phone) {
                    return false
                }
            }
        }

    } catch (e: Exception) {
        return false
        Log.d("PHONE", "Failed to open csv")
    }

    return true
}