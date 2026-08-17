package com.theekshana.mediapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmForm : AppCompatActivity() {
    
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirm_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.confirmRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepository = UserRepository()

        val username = intent.getStringExtra("USERNAME") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val mobile = intent.getStringExtra("MOBILE") ?: ""

        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val footerBack = findViewById<TextView>(R.id.confirmFooterBack)

        footerBack.setOnClickListener {
            finish()
        }

        submitButton.setOnClickListener {
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (password.isNotEmpty() && password == confirmPassword) {
                val newUser = User(username, email, mobile, password)
                userRepository.registerUser(newUser) { success ->
                    if (success) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginForm::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}