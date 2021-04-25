package com.udacity


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, file: String, status: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("file",file)
        putExtra("status", status)
    }

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val statusIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("file",file)
        putExtra("status", status)
    }
    val statusPendingIntent: PendingIntent =
        PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, statusIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.status),
            statusPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
