package com.example.feelingsdiary

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class NotificationFrequencyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_frequency)

        val prefs : SharedPreferences = getSharedPreferences("feelings_diary", Context.MODE_PRIVATE)

        val mFrequencyGroup : RadioGroup = findViewById(R.id.frequency_group)
        val mSaveButton : Button = findViewById(R.id.save_notification_frequency_btn)

        mFrequencyGroup.check(mFrequencyGroup.getChildAt(prefs.getInt("notification_setting", 0)).id)

        // save notification frequency button
        mSaveButton.setOnClickListener {
            val selected : RadioButton = findViewById(mFrequencyGroup.checkedRadioButtonId)

            // store user selected frequency option
            prefs.edit().putInt("notification_setting", mFrequencyGroup.indexOfChild(selected))
                .apply()

            Toast.makeText(
                this@NotificationFrequencyActivity,
                getString(R.string.saved),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
