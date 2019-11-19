package com.example.feelingsdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.title = getString(R.string.settings)

        findViewById<Button>(R.id.change_pass_button).setOnClickListener {
            startActivity(
                Intent(this@SettingsActivity, ChangePasswordActivity::class.java)
            )
        }

        findViewById<Button>(R.id.change_frequency_button).setOnClickListener {
            startActivity(
                Intent(this@SettingsActivity, NotificationFrequencyActivity::class.java)
            )
        }

        findViewById<Button>(R.id.change_notif_time_button).setOnClickListener {
            startActivity(
                Intent(this@SettingsActivity, NotificationTimeActivity::class.java)
            )
        }
    }
}
