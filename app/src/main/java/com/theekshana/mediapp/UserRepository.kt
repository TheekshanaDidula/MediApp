package com.theekshana.mediapp

import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun registerUser(user: User, onComplete: (Boolean) -> Unit) {
        // Use username as key (in a real app, use a unique ID or email hash)
        database.child(user.username).setValue(user)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun loginUser(username: String, password: String, onComplete: (Boolean, User?) -> Unit) {
        database.child(username).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                if (user?.password == password) {
                    onComplete(true, user)
                } else {
                    onComplete(false, null)
                }
            } else {
                onComplete(false, null)
            }
        }.addOnFailureListener {
            onComplete(false, null)
        }
    }
}