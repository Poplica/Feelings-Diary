package com.example.feelingsdiary

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.time.temporal.WeekFields
import java.util.*

private const val DAILY = 0
private const val FEW_DAYS = 1
private const val WEEKLY = 2
private const val NEVER = 3

private const val DAY = 7
private const val WEEK = 14

class NotificationReceiver : BroadcastReceiver() {
    private lateinit var pref : SharedPreferences

    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    override fun onReceive(context: Context, intent: Intent) {
        pref = context.getSharedPreferences("feelings_diary", Context.MODE_PRIVATE)

        // Send notification based on selected frequency
        when (pref.getInt("notification_setting", 0)) {
            DAILY -> processNotification(context, 1)
            FEW_DAYS -> processNotification(context, 2)
            WEEKLY -> processNotification(context, 7)
            NEVER ->
                // increment day's since last opened app by 1
                pref.edit()
                    .putInt("last_entry", pref.getInt("last_entry", 0) + 1)
                    .apply()
        }
    }

    // Determine what type of notification to send based on the limit
    // set by selecting notification frequency
    private fun processNotification(context: Context, limit: Int) {
        val daysSinceLastEntry = pref.getInt("last_entry", 0)
        val daysSinceLastNotification = pref.getInt("last_notification", 0)

        // Send notification if elapsed time has passed
        if (daysSinceLastEntry >= limit && daysSinceLastNotification >= limit) {
             // possible reminder messages
             val allReminders = arrayOf(
                 context.getString(R.string.daily_reminder_1),
                 context.getString(R.string.daily_reminder_2),
                 context.getString(R.string.daily_reminder_3),
                 context.getString(R.string.weekly_reminder_1),
                 context.getString(R.string.weekly_reminder_2)
             )

             // determine what message to send based on the last time the user opened the app
             // Randomize daily/weekly reminder's in correct range
             // Custom reminder for two+ weeks
             when (daysSinceLastEntry) {
                 DAY -> sendNotification(context, allReminders[Random().nextInt(3)])
                 WEEK -> sendNotification(context, allReminders[Random().nextInt(2) + 3])
                 else -> sendNotification(context, context.getString(R.string.long_time_reminder))
            }

            // reset elapsed time
            pref.edit().putInt("last_notification", 0).apply()
        }
        else {
            // update elapsed time
            pref.edit().putInt("last_notification", daysSinceLastNotification + 1).apply()
        }

        // The user still hasn't created an entry, so increment that
        pref.edit().putInt("last_entry", daysSinceLastEntry + 1).apply()
    }

    // Creates and sends a notification based on the given message
    private fun sendNotification(context: Context, message: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelID = context.getString(R.string.channel_id)

        val channel = NotificationChannel(
            channelID,
            context.getString(R.string.channel_title),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = context.getString(R.string.channel_description)

        manager.createNotificationChannel(channel)

        // Generates a pending intent to launch application on user action
        val resultIntent = Intent(context, LoginActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = Notification.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setChannelId(channelID)
            .setContentText(message)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentIntent(pendingIntent)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setCategory(Notification.CATEGORY_REMINDER)
            .build()

        manager.notify(Random().nextInt(Int.MAX_VALUE), notification)
    }
}
