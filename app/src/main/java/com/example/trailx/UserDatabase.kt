package com.example.trailx

import android.util.Log
import com.google.android.gms.common.api.Response
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserDatabase {
    private var database: DatabaseReference = Firebase.database.reference
    var userFinal: User? = null

    fun writeNewUser(user: User) {
        database.child("users").child(user.username).setValue(user)
    }

    fun getUser(username: String) : User? {
        val post = database.child("users").child(username).get().addOnSuccessListener {
            Log.i("firebase", "got value ${it.value}")
        }.addOnFailureListener{
            Log.i("firebase", "error getting data",it)
        }
        val currentStatus = post.result
        return if (currentStatus != null) {
            currentStatus.value as User
        } else null
    }

    fun getUserByEmail(email : String): User? {
        var users: List<User>? = null
        database.child("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    users = result.children.map { snapshot ->
                        snapshot.getValue(User::class.java)!!
                    }
                }
            }
        }
        users.let { users ->
            users?.forEach { user ->
                if (user.email == email) {
                    Log.d("User", "User found")
                    return user
                }
            }
        }
        return null
    }
}