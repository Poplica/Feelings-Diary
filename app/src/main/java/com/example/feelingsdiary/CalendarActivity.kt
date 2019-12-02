package com.example.feelingsdiary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import com.google.firebase.database.DataSnapshot

class CalendarActivity : AppCompatActivity() {
    internal lateinit var mEntryListView: ListView
    internal lateinit var mSelectedDateEntry: ArrayList<JournalEntry>
    internal lateinit var mDatabaseReference: DatabaseReference
    internal var selectedYear : Int = 0
    internal var selectedMonth: Int = 0
    internal var selectedDay : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        mEntryListView = findViewById(R.id.entry_list_view)
        mSelectedDateEntry = ArrayList()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        selectedMonth = Calendar.getInstance().get(Calendar.MONTH)
        selectedYear = Calendar.getInstance().get(Calendar.YEAR)

        var calendar = findViewById<CalendarView>(R.id.calendar_view)
        calendar.maxDate = System.currentTimeMillis()

        showEntries(mDatabaseReference, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        mEntryListView.setOnItemClickListener { parent, view, position, id ->
            val vei = Intent(this@CalendarActivity, ViewEntryActivity::class.java)
            vei.putExtra("entry", mSelectedDateEntry.get(position))
            startActivityForResult(vei, 2)
        }

        calendar.setOnDateChangeListener { _, year, month, day ->
            clear()
            selectedDay = day
            selectedMonth = month
            selectedYear = year

            showEntries(mDatabaseReference, year, month, day)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 2) {
            clear()
            showEntries(mDatabaseReference, selectedYear, selectedMonth, selectedDay)
        }
    }

    fun showEntries(databaseReference: DatabaseReference, year: Int, month: Int, day: Int) {
        val simpleDate = dateAsStored(year, month, day)
        var databaseDate = databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child(simpleDate)
        var entryTimeList = ArrayList<String>()

        databaseDate.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val children = p0!!.children
                children.mapNotNullTo(mSelectedDateEntry) {it.getValue<JournalEntry>(JournalEntry::class.java)}
                if (mSelectedDateEntry.size == 0) {
                    Toast.makeText(applicationContext, "No recorded entries", Toast.LENGTH_LONG).show()
                } else {
                    mSelectedDateEntry.forEach{
                        entryTimeList.add(it.getDate().substring(it.getDate().lastIndexOf(',') + 2) + "\n" + it.getEntry())
                    }
                    var adapter = ArrayAdapter(this@CalendarActivity, R.layout.listview_content, entryTimeList)
                    mEntryListView.adapter = adapter
                }
            }

        })
    }

    fun dateAsStored(year: Int, month: Int, day: Int): String {
        var dateStr = "" + (month + 1) + "-" + day + "-" + year
        var sdf = SimpleDateFormat("MM-dd-yy", Locale.US)
        val simpleDate = sdf.parse(dateStr)

        return sdf.format(simpleDate)
    }

    fun clear() {
        mSelectedDateEntry.clear()
        var adapter = ArrayAdapter(this@CalendarActivity, R.layout.listview_content, ArrayList<String>())
        mEntryListView.adapter = adapter
    }
}
