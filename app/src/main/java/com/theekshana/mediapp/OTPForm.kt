package com.theekshana.mediapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OTPForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.otpRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("USERNAME") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val mobile = intent.getStringExtra("MOBILE") ?: ""

        val footerBack = findViewById<TextView>(R.id.footerBack)
        footerBack.setOnClickListener {
            finish()
        }

        val confirmButton = findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            // After OTP confirmation, go to ConfirmForm (Password entry)
            val intent = Intent(this, ConfirmForm::class.java)
            intent.putExtra("USERNAME", username)
            intent.putExtra("EMAIL", email)
            intent.putExtra("MOBILE", mobile)
            startActivity(intent)
        }
    }
}