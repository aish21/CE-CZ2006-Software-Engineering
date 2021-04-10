package com.example.trailx

//Necessary imports
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeScreen : AppCompatActivity() {
    //Function that is invoked on the creation of the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        supportActionBar?.hide()

        //Active Trails button
        val active_bt_HS = findViewById<Button>(R.id.active_trail_home_bt)
        active_bt_HS.setOnClickListener{
            val intent_active_bt_HS = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_bt_HS)
        }

        //Settings button
        val settings_bt_HS = findViewById<Button>(R.id.settings_bt_home)
        settings_bt_HS.setOnClickListener{
            val intent_settings_bt_HS = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_HS)
        }

        //Discover New Trails button
        val discover_bt_HS = findViewById<Button>(R.id.discover_new_trails_bt_home)
        discover_bt_HS.setOnClickListener{
            val intent_discover_bt_HS = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_bt_HS)
        }

        //My Trails button
        val my_trails_bt_HS = findViewById<Button>(R.id.my_trails_bt_home)
        my_trails_bt_HS.setOnClickListener{
            val intent_my_trails_bt_HS = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_HS)
        }

        //Emergency SOS button
        val emergency_bt_HS = findViewById<Button>(R.id.emergency_sos_bt_home)
        emergency_bt_HS.setOnClickListener{
            val intent_emergency_bt_HS = Intent(this, EmergencySOSScreen::class.java)
            startActivity(intent_emergency_bt_HS)
        }

        //My Account Button
        val my_account_HS = findViewById<Button>(R.id.my_account_home)
        my_account_HS.setOnClickListener{
            val intent_my_account_HS = Intent(this, MyAccountScreen::class.java)
            startActivity(intent_my_account_HS)
        }
    }
}