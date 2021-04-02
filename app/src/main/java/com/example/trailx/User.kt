package com.example.trailx

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username:String = "Username",
    var name:String = "Name",
    var age:Int = -1,
    var gender:String = "Gender",
    var email:String = "Email",
    var height:Int = -1,
    var weight:Int = -1,
    var password:String = "password"
)
