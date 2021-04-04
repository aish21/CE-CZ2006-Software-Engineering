package com.example.trailx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyAccountScreen : AppCompatActivity() {
    private lateinit var fullname_tv: TextView
    private lateinit var age_tv: TextView
    private lateinit var gender_tv: TextView
    private lateinit var email_tv: TextView
    private lateinit var height_tv: TextView
    private lateinit var weight_tv: TextView

    @SuppressLint("LogNotTimber")
    private fun readUserInfo() {
        val userFirebase = Firebase.auth.currentUser
        val database = Firebase.database.reference
        val email = userFirebase?.email
        email_tv.text = email
        var users: List<User>? = null
        database.child("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    users = result.children.map { snapshot ->
                        snapshot.getValue(User::class.java)!!
                    }
                }
                users.let { users ->
                    users?.forEach { user ->
                        if (user.email == email) {
                            Log.d("User", "User found")
                            val name = user.name
                            val age = user.age.toString()
                            val gender = user.gender
                            val height = user.height.toString()
                            val weight = user.weight.toString()
                            fullname_tv.text = name
                            age_tv.text = age
                            gender_tv.text = gender
                            height_tv.text = height
                            weight_tv.text = weight
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account_screen)
        setupUIViews()
        readUserInfo()
        supportActionBar?.hide()
        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_my_account)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_my_account)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_my_account)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_my_account)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_my_account)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_my_account)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }
    }

    private fun setupUIViews() {
        fullname_tv = findViewById<TextView>(R.id.user_fullname_my_account)
        age_tv = findViewById<TextView>(R.id.user_age_my_account)
        gender_tv = findViewById<TextView>(R.id.user_gender_my_account)
        email_tv = findViewById<TextView>(R.id.user_email_my_account)
        height_tv = findViewById<TextView>(R.id.user_height_my_account)
        weight_tv = findViewById<TextView>(R.id.user_weight_my_account)
    }
}