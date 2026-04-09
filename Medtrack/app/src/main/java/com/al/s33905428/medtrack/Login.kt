package com.al.s33905428.medtrack

import android.content.Context
import android.hardware.camera2.CameraExtensionSession
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme
import kotlinx.serialization.Contextual
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.sequences.forEach

class Login : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedtrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    loginScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun loginScreen(innerPadding: PaddingValues) {

    //columns in csv
    val phoneColumnName = "PhoneNumber"
    val passColumnName = "Password"
    val document = "patients.csv"
    val idColumnIndex = 0



    //variables
    var phone by remember { mutableStateOf("0412345678") }
    var pass by remember { mutableStateOf("pass1234") }

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

        //masked input password?
        Spacer(Modifier.height(20.dp))
        TextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") }
        )

        authenticatedPhone = inCSV(phoneColumnName, phone, document, context)
        patientData = validLogin(phone, pass, document, context, phoneColumnName, passColumnName)


        Spacer(Modifier.height(20.dp))

        Button(onClick = {

            //validate that both fields not empty
            if (phone.isNotBlank() and pass.isNotBlank()) {

                //patientData[0] is pID so if its empty that means the password
                //failed to match in validLogin

                if (authenticatedPhone and patientData[0].isBlank()) {
                    errorText = "Password is incorrect."
                    Log.d("CSV_DEBUG", "$patientData")
                    showError = true
                } else if (!authenticatedPhone) {
                    errorText =
                        "Phone number cannot be found in our systems."
                    showError = true
                }
                //both phone number and password in system
                else {

                    //

                    val sharedPref =
                        context.getSharedPreferences("userID", Context.MODE_PRIVATE).edit()
                    sharedPref.putString("ID", patientData[idColumnIndex])
                    sharedPref.apply()

                    errorText = "Successful login."
                    showError = true

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
    }

}

@Composable
fun inCSV(category: String, example: String, document: String, context: Context): Boolean {
    try {

        //processing to find right column
        val inputStream = context.assets.open(document)
        var categoryIndex = 0

        val reader = BufferedReader(InputStreamReader(inputStream))
        val categories = reader.readLine().split(',')

        categoryIndex = categories.indexOf(category)
        //find right value
        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")

                if (values[categoryIndex] == example) {
                    return true

                }
            }
        }

        return false


    } catch (e: Exception) {
        return false
    }
}

@Composable
fun validLogin(phone: String, pass: String, document: String, context: Context, phCat: String, passCat: String): List<String> {
    /*
    phone: phone number to identify
    pass: pass to match
    document: csv file to search
    phCat: column name for the phone numbers
    passCat: column name in csv for the passwords
     */

    try {

        //processing to find right column
        val inputStream = context.assets.open(document)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val line = reader.readLine().split(',')
        val phInd = line.indexOf(phCat)
        val psInd = line.indexOf(passCat)

        Log.d("CSV_DEBUG", "$phInd $psInd")
        //find right value
        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")

                if (values[phInd] == phone) {
                    if (values[psInd] == pass){
                        return values
                    }
                    else{
                        return listOf("")
                    }
                }
            }
        }

        return listOf("")

    } catch (e: Exception) {
        return listOf("")
    }
}


