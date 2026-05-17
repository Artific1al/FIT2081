package com.al.s33905428.medtrack

import android.content.Context
import com.al.s33905428.medtrack.User
import android.hardware.camera2.CameraExtensionSession
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Contextual
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.sequences.forEach


@Composable
fun loginScreen(nav: NavHostController) {

    //columns in csv
    val phoneColumnName = "PhoneNumber"
    val passColumnName = "Password"
    val document = "patients.csv"
    val idColumnIndex = 0
    val nameColumnIndex = 2
    val gson = Gson()


    //variables
    var phone by remember { mutableStateOf("") } //0412345678
    var pass by remember { mutableStateOf("") } //pass1234

    var showError by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    var authenticatedPhone by remember { mutableStateOf(false) }
    var patientData by remember { mutableStateOf(listOf<String>())}


    val context = LocalContext.current

    if (showError) {

        Dialog(
            onDismissRequest = { showError = false },
            content = { Text("$errorText") }

        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        //Login Title
        Text("Login", fontSize = 60.sp)

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )


        Spacer(Modifier.height(20.dp))
        TextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation() // mask input
        )

        Spacer(Modifier.height(20.dp))

        Row(){

        Button(onClick = {

            authenticatedPhone = phoneInCSVorPref( phone, document, context, gson)
            val patientData: User? = validLogin(phone, pass, document, context,  gson)
            Log.d("SHAREDPREF", "null? ${patientData == null}")


            //validate that both fields not empty
            if (phone.isNotBlank() and pass.isNotBlank()) {

                if (authenticatedPhone && (patientData == null)) {
                    errorText = "Incorrect password"
                    showError = true

                } else if (!authenticatedPhone) {
                    errorText =
                        "No account found with this phone number."
                    showError = true
                }
                //both phone number and password in system
                else {

                    if (patientData is User){

                        val sharedPref =
                            context.getSharedPreferences(
                                "logged_in_patient_id",
                                Context.MODE_PRIVATE
                            )
                                .edit()
                        sharedPref.putString("ID", patientData.ID)
                        sharedPref.putString("name", patientData.name)
                        sharedPref.apply()

                        //errorText = "Successful login."
                        //showError = true

                        //navigate to home screen
                        nav.navigate("home")
                    }
                    else{
                        Log.d("LOGINSCREEN", "how not User?")
                    }

                }
            }

            //display modal -> pls enter info
            else {
                errorText = "Phone number or password is blank."
                showError = true

            }
        })
        {
            Text("Login")
        }
            Spacer(Modifier.width(10.dp))

        Button(onClick = { nav.navigate("signUp") })
        {
            Text("Sign Up")
        }
    }
    }

}


fun phoneInCSVorPref(phone: String, document: String, context: Context, gson: Gson): Boolean {

    //in sharedPref
    val gson = gson
    val fileName = "patients.csv"
    val phoneColumn = 1
    val sp = context.getSharedPreferences("users", Context.MODE_PRIVATE)
    val usersGson = sp.getString("users", null)
    val type = object : TypeToken<MutableList<User>>() {}.type

    //vars
    var usersList: MutableList<User> = mutableListOf()
    //Log.d("SHAREDPREF", "got him ${usersGson == null}")

    if (usersGson != null) {
       // Log.d("SHAREDPREF", "usersGSON: $usersGson")
        usersList = gson.fromJson(usersGson, type)
        //Log.d("SHAREDPREF", "${usersList.get(0).phoneNumber}")
    }

    usersList.forEach { user ->
        if (user.phoneNumber == phone){
            return true
        }
    }


    //inCSV
    try {

        //processing to find right column
        val inputStream = context.assets.open(document)

        val reader = BufferedReader(InputStreamReader(inputStream))

        //find right value
        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")
                //Log.d("SHAREDPREF", "$values")

                if (values[phoneColumn] == phone) {
                    return true

                }
            }
        }

        return false


    } catch (e: Exception) {
        return false
    }
}


fun validLogin(phone: String, pass: String, document: String, context: Context, gson: Gson): User? {
    /*
    phone: phone number to identify
    pass: pass to match
    document: csv file to search
    phCat: column name for the phone numbers
    passCat: column name in csv for the passwords
     */


    //in sharedPref
    val gson = gson
    val fileName = "patients.csv"
    val phoneIndex = 1
    val passswordIndex = 3
    val sp = context.getSharedPreferences("users", Context.MODE_PRIVATE)
    val usersGson = sp.getString("users", null)

    val type = object : TypeToken<MutableList<User>>() {}.type

    //vars
    var usersList: MutableList<User> = mutableListOf()

    if (usersGson != null) {
        usersList = gson.fromJson(usersGson, type)
    }

    usersList.forEach { user ->
        if ((user.phoneNumber == phone) &&( user.password == pass)){
            return user
        }
    }



    //inCSV
    try {

        //processing to find right column
        val inputStream = context.assets.open(document)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val line = reader.readLine().split(',')

        var returnUser: User?

        Log.d("SHAREDPREF", "phone: ${phone}\npass: ${pass}")
        //find right value
        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")
                Log.d("SHAREDPREF", "values: ${values}")


                if (values[phoneIndex] == phone) {
                    if (values[passswordIndex] == pass){
                        Log.d("SHAREPREF", "why no return?")

                        returnUser = User(
                            ID = values[0],
                            phoneNumber = values[1],
                            name = values[2],
                            password = values[3]
                        )

                        return returnUser

                        //return newUser

                    }
                    else{
                        //do nothing
                    }
                }
            }
        }

        return null

    } catch (e: Exception) {
        Log.d("VALID_LOGIN", "failed to read csv")
        return null
    }
}



