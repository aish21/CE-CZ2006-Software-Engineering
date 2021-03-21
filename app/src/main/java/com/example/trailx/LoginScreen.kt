package com.example.trailx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        supportActionBar?.hide()

        val login_bt_HS = findViewById<Button>(R.id.login_bt_HS)
        login_bt_HS.setOnClickListener{
            val intent_login_bt_HS = Intent(this, HomeScreen::class.java)
            startActivity(intent_login_bt_HS)
        }
    }
}