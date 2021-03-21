package com.example.trailx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        supportActionBar?.hide()

        val login_bt = findViewById<Button>(R.id.login_bt)
        login_bt.setOnClickListener{
            val intent_login_bt = Intent(this, LoginScreen::class.java)
            startActivity(intent_login_bt)
        }

        val register_bt = findViewById<Button>(R.id.register_bt)
        register_bt.setOnClickListener{
            val intent_register_bt = Intent(this, RegisterScreen::class.java)
            startActivity(intent_register_bt)
        }
    }
}