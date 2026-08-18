package com.theekshana.mediapp

import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun registerUser(user: User, onComplete: (Boolean) -> Unit) {
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

    fun getUser(username: String, onComplete: (User?) -> Unit) {
        database.child(username).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                onComplete(user)
            } else {
                onComplete(null)
            }
        }.addOnFailureListener {
            onComplete(null)
        }
    }
}