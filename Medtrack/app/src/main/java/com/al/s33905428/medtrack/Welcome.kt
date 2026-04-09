package com.al.s33905428.medtrack

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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
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
import com.al.s33905428.medtrack.ui.theme.MedtrackTheme

class Welcome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedtrackTheme {
                Scaffold(/*modifier = Modifier.padding(top = 20.dp)inner padding */) { innerPadding ->
                    welcomeScreen(innerPadding)

                }
            }
        }
    }
}

@Composable
fun welcomeScreen(innerPadding: PaddingValues){

    Column(
        modifier = Modifier.padding(innerPadding),
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

            Button( onClick = {}){
                Text("Login")
            }

            Spacer(Modifier.width(20.dp))

            Button( onClick = {}){
                Text("Sign Up")
            }

        }





    }


}