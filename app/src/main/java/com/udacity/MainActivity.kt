package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var downloadManager: DownloadManager

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (url.isNotEmpty()) {
                download()
                custom_button.setState(ButtonState.Loading)

            } else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG).show()
            }
        }

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val query = id?.let {
                DownloadManager.Query()
                    .setFilterById(it)
            }
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                )
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        if (context != null) {
                            sendNotification(context, url, "Successful")
                            custom_button.setState(ButtonState.Completed)
                        }
                    }
                    DownloadManager.STATUS_FAILED -> {
                        if (context != null) {
                            sendNotification(context, url, "Failed")
                            custom_button.setState(ButtonState.Completed)
                        }
                    }
                }
            }

        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        //private const val URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.bumptech ->
                    if (checked) {
                        url = "https://github.com/bumptech/glide"
                    }
                R.id.udacity ->
                    if (checked) {
                        url = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                    }
                R.id.retrofit ->
                    if (checked) {
                        url = "https://github.com/square/retrofit"
                    }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download"
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }}

    private fun sendNotification(context: Context, file: String, status: String){
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotification(
                context.getText(R.string.download_complete).toString(),
                file,
                status,
                context
            )}

}
