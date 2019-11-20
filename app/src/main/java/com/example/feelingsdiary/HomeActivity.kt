package com.example.feelingsdiary

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

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
        when (item.itemId) {
            R.id.settings_menu -> {
                startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
                return true
            }
            // TODO - Sign out of firebase when logout menu option is selected
            R.id.logout_menu -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
