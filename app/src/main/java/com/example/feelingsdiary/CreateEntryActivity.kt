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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_entry)

        val buttonSubmit = findViewById<View>(R.id.button)
        buttonSubmit.setOnClickListener {
            val userSeekBar = findViewById<View>(R.id.seekBar) as SeekBar
            val userEntry = findViewById<View>(R.id.entry) as EditText
            val userTags = findViewById<View>(R.id.tags) as EditText

            var sdf = SimpleDateFormat("MM-dd-yy", Locale.US)
            val fbDate = Calendar.getInstance().time
            val simpleDate = sdf.format(fbDate)

            // database schema:
            // root ->
                // current user ID ->
                    // date of entries ->
                        // journal entries with fields: date, entry, rating, tags
            val root = FirebaseDatabase.getInstance().reference
            val pointer = root.child(FirebaseAuth.getInstance().currentUser!!.uid).child(simpleDate).push()

            sdf = SimpleDateFormat("EEE, MMM dd, yyyy, h:mm a", Locale.US)
            val complexDate = sdf.format(fbDate)

            // get tags from user input
            lateinit var entryTags: ArrayList<String>
            entryTags = try { // for tag size > 1
                userTags.text.split(",") as ArrayList<String>
            } catch (e : Exception) { // for tag size <= 1
                arrayListOf(userTags.text.toString())
            }
            for (i in 0..(entryTags.size - 1)) {
                entryTags[i] = entryTags[i].trim()
            }

            // serialize object and put in database
            val journalEntry = JournalEntry(
                complexDate,
                userEntry.text.toString(),
                (userSeekBar.progress + 1).toString(),
//                userTags.text.split(",") as ArrayList<String>
                entryTags,
                simpleDate
            )
            pointer.setValue(journalEntry)

            val intent = Intent()
            intent.putExtra("entry", journalEntry)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
