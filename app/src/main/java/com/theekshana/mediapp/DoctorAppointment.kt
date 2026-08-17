package com.theekshana.mediapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class DoctorAppointment : AppCompatActivity() {

    private var selectedDoctor: MaterialCardView? = null
    private var selectedTime: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_appointment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Doctor Selection
        val doctorCards = listOf(
            findViewById<MaterialCardView>(R.id.doctor1),
            findViewById<MaterialCardView>(R.id.doctor2),
            findViewById<MaterialCardView>(R.id.doctor3),
            findViewById<MaterialCardView>(R.id.doctor4)
        )

        doctorCards.forEach { card ->
            card.setOnClickListener {
                selectedDoctor?.setCardBackgroundColor(Color.WHITE)
                card.setCardBackgroundColor(Color.parseColor("#E3F2FD")) // Light blue for selection
                selectedDoctor = card
            }
        }

        // Time Selection
        val timeSlots = listOf(
            findViewById<TextView>(R.id.time9am),
            findViewById<TextView>(R.id.time10am),
            findViewById<TextView>(R.id.time11am),
            findViewById<TextView>(R.id.time2pm),
            findViewById<TextView>(R.id.time3pm),
            findViewById<TextView>(R.id.time4pm)
        )

        timeSlots.forEach { slot ->
            slot.setOnClickListener {
                selectedTime?.setBackgroundResource(R.drawable.edittext_bg)
                slot.setBackgroundColor(Color.parseColor("#25DE9D")) // Green for selection
                selectedTime = slot
            }
        }

        findViewById<Button>(R.id.bookButton).setOnClickListener {
            startActivity(Intent(this, AppointmentSuccess::class.java))
        }
    }
}
