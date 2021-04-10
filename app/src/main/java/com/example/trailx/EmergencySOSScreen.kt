package com.example.trailx

//Necessary imports
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.net.Uri
import android.widget.Toast

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class EmergencySOSScreen : AppCompatActivity() {

    var REQUEST_PHONE_CALL= 1

    //Function that is invoked on the Creation of the Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_s_o_s_screen)

        supportActionBar?.hide()

        //Emergency call button
        val emergency_bt = findViewById<Button>(R.id.emergency_bt)
        emergency_bt.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            }else{
                Emergency_onClick()
            }
        }

        //Home button
        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_emergency)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }

        //Settings button
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_emergency)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }

        //Discover New Trails button
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_emergency)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }

        //Active Trails button
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_emergency)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }

        //My Trails button
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_emergency)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }

        //Music button
        val music_bt_bar = findViewById<Button>(R.id.music_bt_emergency)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }
    }

    //Function that is invoked to place the emergency call
    fun Emergency_onClick() {
        val numberText ="94560494"
        val intent=Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$numberText")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
            return
        }
        startActivity(intent)
    }

    //Function to be executed after permission is granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PHONE_CALL){
            Emergency_onClick()
        }
    }
}
