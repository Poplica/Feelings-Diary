package com.example.feelingsdiary

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import java.util.*

private const val MIDNIGHT: Long = 43200000

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Create Alarm that runs daily to process notifications
        //only create one instance for the application
        if (savedInstanceState == null) {
            val mAlarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // launches the NotificationReceiver class to handle what notification to send
            val intent = Intent(this, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // set alarm to trigger daily notification at midnight
            val time = getSharedPreferences("feelings_diary", MODE_PRIVATE).getLong(
                "notification_time",
                MIDNIGHT
            )

            // not a urgent notification, time can be off by a few hours
            mAlarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                AlarmManager.INTERVAL_DAY + time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

        val buttonAddEntry = findViewById<View>(R.id.add_entry_button)
        buttonAddEntry.setOnClickListener {
            startActivityForResult(Intent(this@HomeActivity, CreateEntryActivity::class.java), 0)
        }

        val buttonGraph = findViewById<View>(R.id.graph_button)
        buttonGraph.setOnClickListener {
            startActivity(Intent(this@HomeActivity, GraphActivity::class.java))
        }

        val buttonCalendar = findViewById<View>(R.id.calendar_button)
        buttonCalendar.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CalendarActivity::class.java))
        }

        val buttonSearch = findViewById<View>(R.id.search_button)
        buttonSearch.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val intent = Intent(this@HomeActivity, ViewEntryActivity::class.java)
            intent.putExtra("entry", data!!.getSerializableExtra("entry"))
            startActivity(intent)
        }
    }

    //Creates a menu in the toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (super.onCreateOptionsMenu(menu)) {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_menu -> {
                startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
                true
            }
            R.id.logout_menu -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
