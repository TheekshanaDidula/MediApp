package com.theekshana.mediapp

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MedicineRepository(context: Context) {
    
    private val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val currentUsername = sharedPref.getString("CURRENT_USER", "guest") ?: "guest"
    
    // Store medicines under the specific user's node
    private val database = FirebaseDatabase.getInstance().getReference("users").child(currentUsername).child("medicines")

    fun saveMedicine(medicine: Medicine, onComplete: (Boolean) -> Unit) {
        database.child(medicine.id).setValue(medicine)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun deleteMedicine(medicineId: String, onComplete: (Boolean) -> Unit) {
        database.child(medicineId).removeValue()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun observeMedicines(onDataChange: (List<Medicine>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val medicines = mutableListOf<Medicine>()
                for (medSnapshot in snapshot.children) {
                    val medicine = medSnapshot.getValue(Medicine::class.java)
                    medicine?.let { medicines.add(it) }
                }
                onDataChange(medicines)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}