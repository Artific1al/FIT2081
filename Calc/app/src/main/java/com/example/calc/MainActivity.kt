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

        firstNumberEditText = findViewById(R.id.val1)
        secondNumberEditText = findViewById(R.id.val2)

        // lesson 6 on ed

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}