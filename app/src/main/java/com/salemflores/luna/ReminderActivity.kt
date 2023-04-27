package com.salemflores.luna

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {

    private lateinit var checkBoxBreakfast: CheckBox
    private lateinit var checkBoxFunActivity: CheckBox
    private lateinit var checkBoxSleeping: CheckBox
    private lateinit var timePicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_reminder)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Channel"
            val descriptionText = "Channel for reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        checkBoxBreakfast = findViewById<CheckBox>(R.id.checkBoxBreakfast)
        checkBoxFunActivity = findViewById<CheckBox>(R.id.checkBoxFunActivity)
        checkBoxSleeping = findViewById<CheckBox>(R.id.checkBoxSleeping)
        timePicker = findViewById(R.id.timePickerReminder)

        val preferences = getPreferences(Context.MODE_PRIVATE)
        val funReminder = preferences.getBoolean("fun_reminder", false)
        val eatReminder = preferences.getBoolean("eat_reminder", false)
        val sleepReminder = preferences.getBoolean("sleep_reminder", false)
        val reminderTime = preferences.getLong("reminder_time", 0)

        // Update UI components based on preferences
        checkBoxBreakfast.isChecked = eatReminder
        checkBoxFunActivity.isChecked = funReminder
        checkBoxSleeping.isChecked = sleepReminder

        val calendar =
            Calendar.getInstance() //val calendar = Calendar.getInstance(Locale.getDefault())

        if (reminderTime != 0L) {
            calendar.timeInMillis = reminderTime
            timePicker.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            timePicker.currentMinute = calendar.get(Calendar.MINUTE)
        } else {
            timePicker.currentHour = 8
            timePicker.currentMinute = 0
        }

        // Set up button click listener
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val editor = preferences.edit()
            editor.putBoolean("fun_reminder", checkBoxFunActivity.isChecked)
            editor.putBoolean("eat_reminder", checkBoxBreakfast.isChecked)
            editor.putBoolean("sleep_reminder", checkBoxSleeping.isChecked)
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
            calendar.set(Calendar.MINUTE, timePicker.currentMinute)
            editor.putLong("reminder_time", calendar.timeInMillis)
            editor.apply()
            setReminders()
            finish()
        }


        //val exitButton = findViewById<Button>(R.id.exit_button)
        //exitButton.setOnClickListener {
        //finish()
        //}

    }

    private fun setReminders() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val workoutIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("message", "It's time for a break! go have some fun :3")
        }
        val eatIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("message", "It's time for some yummy breakfast!")
        }
        val sleepIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("message", "It's time to go to bed!")
        }
        // fun reminder
        if (checkBoxFunActivity.isChecked) {
            val workoutPendingIntent = PendingIntent.getBroadcast(this, 0, workoutIntent, 0)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
            calendar.set(Calendar.MINUTE, timePicker.currentMinute)
            calendar.set(Calendar.SECOND, 0)
            val timeInMillis = calendar.timeInMillis
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, workoutPendingIntent)
        }

        // Breakfast reminder
        if (checkBoxBreakfast.isChecked) {
            val eatPendingIntent = PendingIntent.getBroadcast(this, 1, eatIntent, 0)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val timeInMillis = calendar.timeInMillis
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, eatPendingIntent)
        }

        // Sleeping reminder
        if (checkBoxSleeping.isChecked) {
            val sleepPendingIntent = PendingIntent.getBroadcast(this, 2, sleepIntent, 0)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 22)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val timeInMillis = calendar.timeInMillis
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sleepPendingIntent)
        }


    }
}