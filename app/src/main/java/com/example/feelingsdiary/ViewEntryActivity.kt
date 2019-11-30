package com.example.feelingsdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ViewEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entry)

        val root  = FirebaseDatabase.getInstance().reference
        val pointer = root.child(FirebaseAuth.getInstance().currentUser!!.uid).child(intent.data.toString())

        val date = findViewById<View>(R.id.date) as TextView
//        date.text = pointer.child()
    }
}
