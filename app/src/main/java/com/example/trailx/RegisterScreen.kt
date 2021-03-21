package com.example.trailx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        supportActionBar?.hide()

        val reg_bt_HS = findViewById<Button>(R.id.register_bt_HS)
        reg_bt_HS.setOnClickListener {
            val intent_reg_bt_HS = Intent(this, HomeScreen::class.java)
            startActivity(intent_reg_bt_HS)
        }
    }
}