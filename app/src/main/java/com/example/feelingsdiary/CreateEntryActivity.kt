package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_graph.view.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_entry)

//        val intent = Intent()
//        intent.putExtra("entry", journalEntry)
        val buttonSubmit = findViewById<View>(R.id.button)
        buttonSubmit.setOnClickListener {
            val userSeekBar = findViewById<View>(R.id.seekBar) as SeekBar
            val userEntry = findViewById<View>(R.id.entry) as EditText
            val userTags = findViewById<View>(R.id.tags)

            var sdf = SimpleDateFormat("MM-dd-yy", Locale.US)
            val fbDate = Calendar.getInstance().time
            val simpleDate = sdf.format(fbDate)

            var root = FirebaseDatabase.getInstance().reference
            val pointer = root.child(FirebaseAuth.getInstance().currentUser!!.uid).child(simpleDate).push()

            sdf = SimpleDateFormat("EEE, MMM dd, yyyy, h:mm a", Locale.US)
            val entryDate = sdf.format(fbDate)

            val journalEntry = JournalEntry(
                entryDate,
                userEntry.text.toString(),
                (userSeekBar.progress + 1).toString(),
                userTags.toString().split(",") as ArrayList<String>
            )
            pointer.setValue(journalEntry)

            val intent = Intent()
            intent.putExtra("date", journalEntry)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
