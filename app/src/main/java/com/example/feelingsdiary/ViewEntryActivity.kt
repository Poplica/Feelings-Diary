package com.example.feelingsdiary

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_entry.*
import java.lang.StringBuilder

class ViewEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entry)

        val root = FirebaseDatabase.getInstance().reference
        val pointer = root.child(FirebaseAuth.getInstance().currentUser!!.uid)

        val dateText = findViewById<View>(R.id.date) as TextView
        val moodRating = findViewById<View>(R.id.mood_scale) as RatingBar
        val tags = findViewById<View>(R.id.recorded_tags) as TextView
        val thoughtsText = findViewById<View>(R.id.recorded_thoughts) as TextView
        val entry = intent.getSerializableExtra("entry") as JournalEntry

        dateText.text = entry.getDate()
        moodRating.rating = entry.getRating().toFloat()
        tags.text = entry.getTags().joinToString()
        thoughtsText.text = entry.getEntry()

        val deleteAlert = AlertDialog.Builder(this@ViewEntryActivity)
        deleteAlert.setTitle(R.string.delete_prompt)

        deleteAlert.setPositiveButton(R.string.yes_delete) {dialog, which ->
            deleteEntry(pointer, entry)
        }

        deleteAlert.setNegativeButton(R.string.cancel) { dialog, which ->
            dialog.cancel()
        }

        val buttonDeleteEntry = findViewById<View>(R.id.delete_entry_button)
        buttonDeleteEntry.setOnClickListener {
            deleteAlert.show()
        }
    }

    private fun deleteEntry(pointer: DatabaseReference, entry: JournalEntry) {


        finish()
    }
}
