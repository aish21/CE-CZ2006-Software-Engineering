package com.example.trailx

//Necessary imports
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class StartScreen : AppCompatActivity() {

    //Function that specifies what happens when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        supportActionBar?.hide()

        //Login button
        val login_bt = findViewById<Button>(R.id.login_bt)
        login_bt.setOnClickListener{
            val intent_login_bt = Intent(this, LoginScreen::class.java)
            startActivity(intent_login_bt)
        }

        //Register button
        val register_bt = findViewById<Button>(R.id.register_bt)
        register_bt.setOnClickListener{
            val intent_register_bt = Intent(this, RegisterScreen::class.java)
            startActivity(intent_register_bt)
        }
    }
}