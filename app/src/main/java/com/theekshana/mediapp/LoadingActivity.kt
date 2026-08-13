package com.theekshana.mediapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loadingRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Wait for 2 seconds and then move to LoginForm (or HomePage if logged in)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginForm::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}