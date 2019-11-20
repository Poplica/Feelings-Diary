package com.example.feelingsdiary

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
}
