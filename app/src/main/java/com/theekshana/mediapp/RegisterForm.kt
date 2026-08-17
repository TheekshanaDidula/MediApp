package com.theekshana.mediapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<FrameLayout>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LoginForm::class.java)
            startActivity(intent)
        }

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val mobileInput = findViewById<EditText>(R.id.mobileInput)
        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val mobile = mobileInput.text.toString().trim()

            var isValid = true

            if (username.isEmpty()) {
                usernameInput.error = "Please enter valid details"
                isValid = false
            }

            if (email.isEmpty()) {
                emailInput.error = "Please enter valid details"
                isValid = false
            }

            if (mobile.isEmpty()) {
                mobileInput.error = "Please enter valid details"
                isValid = false
            }

            if (isValid) {
                val intent = Intent(this, OTPForm::class.java)
                startActivity(intent)
            }
        }
    }
}