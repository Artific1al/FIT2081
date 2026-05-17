package com.al.s33905428.medtrack


import android.R
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.sequences.forEach

//class Symptoms : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MedtrackTheme {
//
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                    val context = LocalContext.current
//                    val fileName = "symptoms.csv"
//
//                    Column() {
//                        symptomsTop(innerPadding)
//                        var symptoms = sortedSymptoms(innerPadding, context, fileName).reversed()
//                        symptomsBot(symptoms)
//                    }
//                }
//            }
//        }
//    }


    @Composable
    fun symptomsScreen(nav: NavHostController){
        val context = LocalContext.current
        val fileName = "symptoms.csv"

        Scaffold(
            bottomBar = { navBar(nav)} ) { innerPadding ->

            Column(Modifier.padding(innerPadding)) {
                var context = LocalContext.current
                symptomsTop(context)
                var symptoms = sortedSymptoms(context, fileName).reversed()
                symptomsBot(symptoms)
            }
        }

    }





    data class csvSymptom(
        val ID: String,
        val category: String,
        val severity: Int,
        val notes: String,
        val date: String
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun symptomsTop(context: Context) {

        var topExpanded by remember { mutableStateOf(false) }
        var symptom by remember { mutableStateOf("") }
        var symptomText by remember { mutableStateOf("Click on dots to enter") }
        var symptomErrorText by remember { mutableStateOf("")}
        var sliderVal by remember { mutableStateOf(1) }

        var addNotes by remember { mutableStateOf("") }
        var dateTime by remember { mutableStateOf("") }
        var sbHost = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var showDate by remember { mutableStateOf(false) }
        var showTime by remember { mutableStateOf(false)}
        var datePicker = rememberDatePickerState()
        var timePicker = rememberTimePickerState()
        var charLimit by remember { mutableStateOf(200) }
        var colorDescent = lerp(Color.Red, Color.Green, ((sliderVal-0.1f)/10f))


        Column() {

            if (showDate) {

                Dialog(
                    onDismissRequest = {
                        showDate = false
                        showTime = true
                    }
                ) {
                    Column() {
                        DatePicker(state = datePicker)
                        Button(
                            onClick = {
                                showDate = false
                                showTime = true
                            }
                        ) {
                            Text("Select Time")
                        }
                    }
                }
            }

            if (showTime){
                Dialog(
                    onDismissRequest ={ showTime = false}
                ){
                    Column(){
                        TimePicker(timePicker)
                        Spacer(Modifier.height(100.dp))
                        Button(
                            onClick = { showTime = false }
                        ) {
                            Text("Save Time and Date")
                        }

                    }
                }
            }

            SnackbarHost(hostState = sbHost)
            //Spacer(Modifier.height(.dp))

            Text("Log Symptoms", fontSize = 20.sp)

            Row() {

                IconButton(
                    onClick = { topExpanded = true }
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Open Menu",)
                }

                Spacer(Modifier.width(5.dp))

                Text("Symptom Type: $symptomText")
            }


            DropdownMenu(
                expanded = topExpanded,
                onDismissRequest = { topExpanded = false }) {

                DropdownMenuItem(
                    text = { Text("Pain") },
                    onClick = {
                        topExpanded = false
                        symptom = "Pain"
                        symptomText = symptom
                    }
                )

                DropdownMenuItem(
                    text = { Text("Nausea") },
                    onClick = {
                        topExpanded = false
                        symptom = "Nausea"
                        symptomText = symptom
                    }
                )

                DropdownMenuItem(
                    text = { Text("Dizziness") },
                    onClick = {
                        topExpanded = false
                        symptom = "Dizziness"
                        symptomText = symptom
                    }
                )
                DropdownMenuItem(
                    text = { Text("Fatigue") },
                    onClick = {
                        topExpanded = false
                        symptom = "Fatigue"
                        symptomText = symptom
                    }
                )

                DropdownMenuItem(
                    text = { Text("Headache") },
                    onClick = {
                        topExpanded = false
                        symptom = "Headache"
                        symptomText = symptom
                    }
                )
                DropdownMenuItem(
                    text = { Text("Skin Reaction") },
                    onClick = {
                        topExpanded = false
                        symptom = "Skin Reaction"
                        symptomText = symptom
                    }
                )

                DropdownMenuItem(
                    text = { Text("Other") },
                    onClick = {
                        topExpanded = false
                        symptom = "Other"
                        symptomText = symptom
                    }
                )

            }

            Text(symptomErrorText, color = Color.Red, fontSize =8.sp)

            Spacer(Modifier.height(5.dp))


            Text("$sliderVal / 10")
            Box(Modifier.height(30.dp)) {
                Slider(
                    value = sliderVal.toFloat(),
                    onValueChange = { sliderVal = it.toInt()},
                    steps = 99,
                    valueRange = 1f..10f,
                    colors = SliderDefaults.colors(
                        activeTrackColor = colorDescent
                    )
                )
            }

            Spacer(Modifier.height(20.dp))
            TextField(
                value = addNotes,
                onValueChange = {
                    if(it.length < 200){
                        addNotes = it
                    charLimit = 200 - it.length}},
                label = { Text("Enter any additional notes") },
            )
            Text("Character Limit: $charLimit", color= Color.Red, fontSize = 8.sp)

            Spacer(Modifier.height(5.dp))

            Row(){
            //when did this symptom occur
            Button(
                onClick = {
                    showDate = true

                }
            ) {
                Text("Open Date & Time Pickers")
            }



                Spacer(Modifier.height(5.dp))
                Box() {
                    Button(
                        onClick = {
                            symptomErrorText = ""

                            if (symptom.isEmpty()) {
                                symptomErrorText = " Symptom field cannot be empty."
                            }

                            //validated

                                scope.launch {
                                    if (symptom.isNotEmpty()) {
                                sbHost.showSnackbar("symptom added!")
                            }

                                //val sympSP = context.getSharedPreferences("logged_in_patient_id", Context.MODE_PRIVATE)


//                            val medSP = context.getSharedPreferences("medications", Context.MODE_PRIVATE).edit()
//                            val medSPGet = context.getSharedPreferences("medications", Context.MODE_PRIVATE)
//
//                            mutableStateListOf<Medication>()
//
//                            //sample GSon to data class list example code found online and modified
//                            val medsGson = medSPGet.getString("medications", null)
//                            val type = object : TypeToken<MutableList<Medication>>() {}.type
//                            var medList: MutableList<Medication> = mutableStateListOf<Medication>()
//
//                            if (medsGson != null){
//                                medList = gson.fromJson(medsGson, type)
//                            }
//                            medList.add(newMed)
//
//                            //now we have the list and can add it back to shared pref
//                            val gsonString = gson.toJson(medList)
//                            medSP.putString("medications",gsonString)
//                            medSP.apply()

                            }
                        }


                    ) {
                        Text("Save Symptom")

                    }
                    SnackbarHost(hostState = sbHost)
                }
                //in box to load snackbar

            }
        }
    }



    @Composable
    fun symptomsBot(symptoms: List<csvSymptom>) {

        Column() {
            Spacer(Modifier.height(40.dp))
            Text("Recorded Symptoms", fontSize =10.sp)

            if(symptoms.size == 0){
                Text("No symptoms logged yet")
            }

            LazyColumn() {


                items(symptoms) { current ->
                    //scrollable lazy list

                    Spacer(Modifier.height(15.dp))


                    if(current.severity < 4) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Green.copy(alpha=0.3f)
                            )
                        )
                        {

                            Text("Category: ${current.category}")
                            Spacer(Modifier.height(10.dp))

                            Text("Date/Time: ${current.date}")
                            Spacer(Modifier.height(10.dp))

                            Text("Severity: ${current.severity}")
                            Spacer(Modifier.height(10.dp))

                            Text("Notes: ${current.notes}")
                            Spacer(Modifier.height(10.dp))

                        }
                    }
                       if((current.severity < 7) and (current.severity > 3) ){

                            Card(colors = CardDefaults.cardColors(
                                containerColor = Color.Yellow.copy(alpha=0.3f)))
                            {

                                Text("Category: ${current.category}")
                                Spacer(Modifier.height(10.dp))

                                Text("Date/Time: ${current.date}")
                                Spacer(Modifier.height(10.dp))

                                Text("Severity: ${current.severity}")
                                Spacer(Modifier.height(10.dp))

                                Text("Notes: ${current.notes}")
                                Spacer(Modifier.height(10.dp))

                            }
                           }
                        else {

                           Card(
                               colors = CardDefaults.cardColors(
                                   containerColor = Color.Red.copy(alpha=0.3f)
                               )
                           )
                           {

                               Text("Category: ${current.category}")
                               Spacer(Modifier.height(10.dp))

                               Text("Date/Time: ${current.date}")
                               Spacer(Modifier.height(10.dp))

                               Text("Severity: ${current.severity}")
                               Spacer(Modifier.height(10.dp))

                               Text("Notes: ${current.notes}")
                               Spacer(Modifier.height(10.dp))


                           }
                       }


                }


            }
        }
    }


    @Composable
    fun sortedSymptoms(
        context: Context,
        fileName: String
    ): List<csvSymptom> {

        //sharedPref


        var sortedSymptomList: List<csvSymptom> = mutableListOf<csvSymptom>()
        val symptomList: MutableList<csvSymptom> = mutableListOf<csvSymptom>()




        try {

            //generate reader
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val symptomList: MutableList<csvSymptom> = mutableListOf<csvSymptom>()

            val sharedPref =
                context.getSharedPreferences("logged_in_patient_id", Context.MODE_PRIVATE)
            val currentId = sharedPref.getString("ID", "P1001")

            //find corresponding values
            reader.useLines { lines ->
                lines.drop(1).forEach { line ->
                    val values = line.split(",")

                    //
                    if (values[0] == currentId) {

                        //add all data
                        symptomList.add(
                            csvSymptom(
                                ID = values[0],
                                category = values[1],
                                severity = values[2].toInt(),
                                notes = values[3],
                                date = values[4]
                            )

                        )
                    }
                }
            }



            val formatter = SimpleDateFormat("yyyy-MM-dd h:mm", Locale.ENGLISH)




            sortedSymptomList = symptomList.sortedBy { row ->
                formatter.parse(row.date)


            }

        } catch (e: Exception) {
            Log.d("NOT REACHED", "sdf")

        }

        return sortedSymptomList
    }



