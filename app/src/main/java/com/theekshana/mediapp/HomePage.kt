package com.theekshana.mediapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.appointmentCard).setOnClickListener {
            startActivity(Intent(this, DoctorAppointment::class.java))
        }

        findViewById<View>(R.id.medicineCard).setOnClickListener {
            startActivity(Intent(this, Medicinemanage1Page::class.java))
        }

        findViewById<View>(R.id.profileIcon).setOnClickListener {
            startActivity(Intent(this, UserProfile::class.java))
        }
    }
}
