package com.example.feelingsdiary

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast

class NotificationTimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_time)

        val prefs : SharedPreferences = getSharedPreferences("feelings_diary", MODE_PRIVATE)

        val mTimePicker : TimePicker = findViewById(R.id.notification_time)
        val mSaveButton : Button = findViewById(R.id.save_notification_time_btn)

        // save notification time button
        mSaveButton.setOnClickListener {
            val hour = (mTimePicker.hour * 3600000).toLong()
            val minute = (mTimePicker.minute * 60000).toLong()

            // store user selected time option
            prefs.edit().putLong("notification_time", hour + minute).apply()

            Toast.makeText(
                this@NotificationTimeActivity,
                getString(R.string.restart_app),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
