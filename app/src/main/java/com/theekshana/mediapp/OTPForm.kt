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

        val footerBack = findViewById<TextView>(R.id.footerBack)
        footerBack.setOnClickListener {
            val intent = Intent(this, RegisterForm::class.java)
            startActivity(intent)
        }

        val confirmButton = findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val intent = Intent(this, LoginForm::class.java)
            startActivity(intent)
        }
    }
}