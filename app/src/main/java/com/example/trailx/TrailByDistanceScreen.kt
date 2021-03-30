package com.example.trailx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_trail_by_distance_screen.*

abstract class TrailByDistanceScreen : AppCompatActivity() {

    abstract var dist_value:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_by_distance_screen)

        dist_value = findViewById<EditText>(R.id.trail_by_dist_value_trails_by_dist)

        supportActionBar?.hide()
        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_trails_by_dist)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_trails_by_dist)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_trails_by_dist)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_trails_by_dist)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_trails_by_dist)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_trails_by_dist)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }
    }
}

