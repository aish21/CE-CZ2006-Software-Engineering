package com.example.trailx

//Necessary imports
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//Class to act as an interface between the application and Firebase
class UserDatabase {
    //Database reference to the Firebase database
    private var database: DatabaseReference = Firebase.database.reference
    var userFinal: User? = null

    //Function to add a new user to the database
    fun writeNewUser(user: User) {
        database.child("users").child(user.username).setValue(user)
    }
}