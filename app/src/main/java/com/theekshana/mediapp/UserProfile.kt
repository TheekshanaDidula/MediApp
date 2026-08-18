package com.theekshana.mediapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserProfile : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepository = UserRepository()

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val currentUsername = sharedPref.getString("CURRENT_USER", "") ?: ""

        val profileName = findViewById<TextView>(R.id.profileName)
        val detailUsername = findViewById<TextView>(R.id.detailUsername)
        val detailEmail = findViewById<TextView>(R.id.detailEmail)
        val detailMobile = findViewById<TextView>(R.id.detailMobile)
        val logoutButton = findViewById<AppCompatButton>(R.id.logoutButton)
        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        if (currentUsername.isNotEmpty()) {
            userRepository.getUser(currentUsername) { user ->
                user?.let {
                    profileName.text = it.username
                    detailUsername.text = it.username
                    detailEmail.text = it.email
                    detailMobile.text = it.mobile
                }
            }
        }

        logoutButton.setOnClickListener {
            // Clear session
            sharedPref.edit().remove("CURRENT_USER").apply()
            
            // Go back to login
            val intent = Intent(this, LoginForm::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
