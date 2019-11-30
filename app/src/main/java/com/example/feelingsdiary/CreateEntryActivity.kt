package com.example.feelingsdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.R
import android.widget.Spinner



class CreateEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_entry)

        val arraySpinner = arrayOf("Sad", "Depressed", "Angry", "Frustrated")
        val s = findViewById(R.id.emotionSpinner) as Spinner
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        s.adapter = adapter
    }
}
