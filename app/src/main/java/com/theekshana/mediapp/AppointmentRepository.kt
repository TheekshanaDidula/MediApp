package com.theekshana.mediapp

import android.content.Context
import com.google.firebase.database.FirebaseDatabase

class AppointmentRepository(context: Context) {
    
    private val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val currentUsername = sharedPref.getString("CURRENT_USER", "guest") ?: "guest"
    
    // Store appointments under the specific user's node
    private val database = FirebaseDatabase.getInstance().getReference("users").child(currentUsername).child("appointments")

    fun saveAppointment(appointment: Appointment, onComplete: (Boolean) -> Unit) {
        database.child(appointment.id).setValue(appointment)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
