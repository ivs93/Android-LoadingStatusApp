package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val intent : Intent = intent

        val fileName: String? = intent.getStringExtra("file")
        val status: String? = intent.getStringExtra("status")

        val fab : Button = findViewById(R.id.fab)
        val fileNameTV: TextView = findViewById(R.id.file_name)
        val statusTV: TextView = findViewById(R.id.status)

        fileName?.let {
            fileNameTV.text = fileName
            statusTV.text= status
        }

        fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelNotifications()
    }

}
