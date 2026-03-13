package com.example.week1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //We removed some boilerplate codes for the sake of simplicity

    private var clickCounter: Int = 0 //counts the number of clicks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //We removed some boilerplate code for the sake of simplicity..

        clickCounter=0;  //initialize the counter
        val myButton: Button = findViewById(R.id.clickMeButton) //find the button by its id

        myButton.setOnClickListener { //set a click listener on the button
            onButtonClick(it)
            //handleClick() //call the handleClick method when the button is clicked
        }
    }
//    private fun handleClick(){
//        clickCounter++ //increment the counter
//        showToast("button clicked $clickCounter times") //show a toast message with the click count
//    }

    public fun onButtonClick(view: View){
        clickCounter++
        showToast("button clicked $clickCounter times")
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show() //show a toast message with the given message
    }
}