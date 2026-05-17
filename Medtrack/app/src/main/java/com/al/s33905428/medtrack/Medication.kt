package com.al.s33905428.medtrack

import android.R
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api


import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.sql.Time
import kotlin.and

//class Medication : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MedtrackTheme {
//                Scaffold(modifier = Modifier.fillMaxSize().padding()) { innerPadding ->
//                    medScreen(innerPadding)
//                }
//            }
//        }
//    }
//}


data class Medication @OptIn(ExperimentalMaterial3Api::class) constructor(
    val ID: String,
    val medName: String,
    val dosage: String,
    val frequency: String,
    val hours: Int,
    val minutes: Int,
    val type: String,
    val addNotes: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun medScreen(nav: NavHostController) {

    var medName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    val currentTime = Calendar.getInstance()
    var timeInput = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    var medType by remember { mutableStateOf("") }
    var medNotes by remember { mutableStateOf("") }

    var frequencyExpanded by remember { mutableStateOf(false) }
    var medTypeExpanded by remember { mutableStateOf(false) }
    var frequencyText by remember { mutableStateOf("Click on dots to select") }
    var medTypeText by remember { mutableStateOf("Click on dots to select") }
    var showTimePicker by remember { mutableStateOf(false)}

    var medNameErrorText by remember { mutableStateOf("")}
    var dosageErrorText by remember { mutableStateOf("")}
    var frequencyErrorText by remember { mutableStateOf("")}
    var medTypeErrorText by remember { mutableStateOf("")}
    var allValid = false

    val context = LocalContext.current
    val gson = Gson()

    var sbHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        bottomBar = { navBar(nav) }) { innerPadding ->

        if(showTimePicker){
        Dialog(
            onDismissRequest = { showTimePicker = false},
            content =  {
                TimePicker(
                    state=timeInput,

                )
            }
        )
        }

        Column(Modifier.fillMaxWidth().padding(innerPadding)) {

            Text(
                "Add Medication",
                fontSize = 20.sp
            )
            Spacer(Modifier.height(5.dp))


            Text("Medication Name")
            TextField(
                value = medName,
                onValueChange = { medName = it }
            )
            Text("$medNameErrorText", color= Color.Red, fontSize = 8.sp)
            Spacer(Modifier.height(15.dp))


            Text("Dosage")
            TextField(
                value = dosage,
                onValueChange = { dosage = it }
            )
            Text("$dosageErrorText", color= Color.Red, fontSize = 8.sp)
            Spacer(Modifier.height(5.dp))

            Text("Frequency: ${frequencyText}")
            Row() {

                IconButton(
                    onClick = { frequencyExpanded = true }
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Open Menu",)
                }

                Spacer(Modifier.width(10.dp))


            }

            DropdownMenu(
                expanded = frequencyExpanded,
                onDismissRequest = { frequencyExpanded = false }) {


                DropdownMenuItem(
                    text = { Text("Once Daily") },
                    onClick = {
                        frequencyExpanded = false
                        frequency = "Once Daily"
                        frequencyText = frequency
                    }
                )

                DropdownMenuItem(
                    text = { Text("Twice Daily") },
                    onClick = {
                        frequencyExpanded = false
                        frequency = "Twice Daily"
                        frequencyText = frequency
                    }
                )

                DropdownMenuItem(
                    text = { Text("Three Times Daily") },
                    onClick = {
                        frequencyExpanded = false
                        frequency = "Three Times Daily"
                        frequencyText = frequency
                    }
                )

                DropdownMenuItem(
                    text = { Text("As Needed") },
                    onClick = {
                        frequencyExpanded = false
                        frequency = "As Needed"
                        frequencyText = frequency
                    }
                )

            }
            Text("$frequencyErrorText", color= Color.Red, fontSize = 8.sp)
            Spacer(Modifier.height(5.dp))



            Text("Medication Type: ${medTypeText}")
            Text("$medTypeErrorText", color= Color.Red, fontSize = 8.sp)

            Row() {

                IconButton(
                    onClick = { medTypeExpanded = true }
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Open Menu",)
                }

                Box(
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                ) {

                    Spacer(Modifier.height(20.dp))

                    Row() {
                        Button({
                            showTimePicker = true

                        }
                        ) {
                            Text("Open time picker")
                        }

                        Spacer(Modifier.width(10.dp))
                        Text("Hour: ${timeInput.hour}\nMinutes:${timeInput.minute}")

                    }



                }

                Spacer(Modifier.width(5.dp))
            }

            DropdownMenu(
                expanded = medTypeExpanded,
                onDismissRequest = { medTypeExpanded = false }) {

                DropdownMenuItem(
                    text = { Text("Tablet") },
                    onClick = {
                        medTypeExpanded = false
                        medType = "Tablet"
                        medTypeText = medType
                    }
                )
                DropdownMenuItem(
                    text = { Text("Capsule") },
                    onClick = {
                        medTypeExpanded = false
                        medType = "Capsule"
                        medTypeText = medType
                    }
                )
                DropdownMenuItem(
                    text = { Text("Liquid") },
                    onClick = {
                        medTypeExpanded = false
                        medType = "Liquid"
                        medTypeText = medType
                    }
                )
                DropdownMenuItem(
                    text = { Text("Injection") },
                    onClick = {
                        medTypeExpanded = false
                        medType = "Injection"
                        medTypeText = medType
                    }
                )
                DropdownMenuItem(
                    text = { Text("Topical") },
                    onClick = {
                        medTypeExpanded = false
                        medType = "Topical"
                        medTypeText = medType
                    }
                )
                DropdownMenuItem(
                    text = { Text("Other") },
                    onClick = {
                        medTypeExpanded = false
                        medType = "Other"
                        medTypeText = medType
                    }
                )

            }
            Spacer(Modifier.height(5.dp))

            Text("Notes (Optional)")
            TextField(
                value = medNotes,
                onValueChange = { medNotes = it }
            )

            Spacer(Modifier.height(5.dp))


            Row(Modifier.fillMaxWidth()) {

                Box() {
                    Button(
                        onClick = {

                            //reset vars
                            medNameErrorText = ""
                            medTypeErrorText = ""
                            dosageErrorText = ""
                            frequencyErrorText = ""
                            allValid = true

                            //validateMedInput
                            if (medName.isEmpty()){
                                medNameErrorText = "Medicine Name cannot be empty."
                                allValid = false
                            }
                            if (medType.isEmpty()) {
                                medTypeErrorText = "Medicine Type cannot be empty."
                                allValid = false
                            }

                            if (dosage.isEmpty()) {
                                dosageErrorText = "Medicine Dosage cannot be empty."
                                allValid = false
                            }

                            if (frequency.isEmpty()) {
                                frequencyErrorText = "Medicine frequency cannot be empty."
                                allValid = false
                            }

                                //dosage regex
                                // \d+ (\.\d+) digit/s optional(decimal more digits) then (mg or ml or g) $ eol
                                val pattern = Regex("""^\d+(\.\d+)?(mg|ml|g)$""")
                                if (!pattern.matches(dosage)) {
                                    allValid = false
                                    if (dosageErrorText == "") {
                                        dosageErrorText =
                                            "Must follow format: Digits (optional decimal or digits) measurement"
                                    }
                                    dosageErrorText += "\nMust follow format: Digits (optional decimal or digits) measurement"
                                }




                            if(allValid){

                                val userSP = context.getSharedPreferences("logged_in_patient_id", Context.MODE_PRIVATE)
                                var patientID = userSP.getString("ID", "P10000") ?: "P10000"

                                val medSP = context.getSharedPreferences("medications", Context.MODE_PRIVATE).edit()
                                val medSPGet = context.getSharedPreferences("medications", Context.MODE_PRIVATE)

                                var newMed = Medication(
                                    ID = patientID,
                                    medName = medName,
                                    dosage = dosage,
                                    frequency = frequency,
                                    hours = timeInput.hour,
                                    minutes = timeInput.minute,
                                    type = medType,
                                    addNotes = medNotes
                                )
                                mutableStateListOf<Medication>()

                                //sample GSon to data class list example code found online and modified
                                val medsGson = medSPGet.getString("medications", null)
                                val type = object : TypeToken<MutableList<Medication>>() {}.type
                                var medList: MutableList<Medication> = mutableStateListOf<Medication>()

                                if (medsGson != null){
                                    medList = gson.fromJson(medsGson, type)
                                }
                                medList.add(newMed)

                                //now we have the list and can add it back to shared pref
                                val gsonString = gson.toJson(medList)
                                medSP.putString("medications",gsonString)
                                medSP.apply()

                                scope.launch {
                                    sbHost.showSnackbar("New Medicine saved")
                                }

                                nav.navigate("home")

                            }
                        },
                        ) {
                        Text("Save new medication")
                    }

                    SnackbarHost(hostState = sbHost)
                }

                Spacer(Modifier.width(20.dp))
                Button(
                    onClick = {

                        medName = ""
                        dosage = ""
                        frequency = ""
                        medType = ""
                        medNotes = ""
                        medTypeText = "Click on dots to select"
                        frequencyText = "Click on dots to select"
                        //timePicker is left at previous state


                    },

                    ) {
                    Text("Clear Fields")
                }

            }


        }
    }
}





