package com.example.calc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //references to the two edit text numbers
    lateinit var firstNumberEditText: EditText
    lateinit var secondNumberEditText: EditText

    //references to the three buttons (two operations and one clear)
    lateinit var addButton: Button
    lateinit var subButton: Button
    lateinit var clearButton: Button

    //reference to the text view to display the result
    lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addButton = findViewById(R.id.addButton)
        subButton = findViewById(R.id.subButton)
        clearButton = findViewById(R.id.clearButton)

        firstNumberEditText = findViewById(R.id.val1)
        secondNumberEditText = findViewById(R.id.val2)
        resultTextView = findViewById(R.id.Answer)


        addButton.setOnClickListener {
            calculateResult("add")
        }

        subButton.setOnClickListener {
            calculateResult("sub")
        }

        clearButton.setOnClickListener {
            clearAll()
        }
        // lesson 6 on ed
    }


    fun calculateResult(opt: String){

            val num1Str = firstNumberEditText.text.toString()
            val num2Str = secondNumberEditText.text.toString()

        if (num1Str.isEmpty() || num2Str.isEmpty()){
            resultTextView.text = "Please enter both numbers"
            return
        }

        var result: Int = 0
        val num1 = num1Str.toInt()
        val num2 = num2Str.toInt()

        if (opt == "add"){
            result = num1 + num2
        }
        else{
            result = num1 - num2
        }

        resultTextView.text = "Result: $result"

        }

        fun clearAll() {
            firstNumberEditText.text.clear()
            secondNumberEditText.text.clear()
            resultTextView.text = ""
        }



    }


