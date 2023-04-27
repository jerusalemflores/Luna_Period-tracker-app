package com.salemflores.luna

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message")
        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle("Luna App")
            .setContentText(message)
            .setSmallIcon(R.drawable.lunacat)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(0, notification)
    }

}