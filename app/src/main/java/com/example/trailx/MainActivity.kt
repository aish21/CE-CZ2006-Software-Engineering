package com.example.trailx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hide the action bar if its there
        supportActionBar?.hide()
        Timer().schedule(3000){
            val intent = Intent(this@MainActivity, StartScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}