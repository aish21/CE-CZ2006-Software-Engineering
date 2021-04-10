package com.example.trailx

//Necessary imports
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DiscoverNewTrailsScreen : AppCompatActivity() {
    //Function that is invoked on creation of the Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover_new_trails_screen)

        supportActionBar?.hide()

        //Home Button
        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_discover_new_trail)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }

        //Settings button
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_discover_new_trail)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }

        //Discover New Trails button
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_discover_new_trail)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }

        //Active trails button
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_discover_new_trail)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }

        //My Trails button
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_discover_new_trail)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }

        //Music Button
        val music_bt_bar = findViewById<Button>(R.id.music_bt_discover_new_trail)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }

        //Trails by Distance button
        val trail_by_dist_bt = findViewById<Button>(R.id.trail_by_distance_bt_discover_new_trail)
        trail_by_dist_bt.setOnClickListener {
            val intent_trail_by_dist_bt = Intent(this, TrailByDistanceScreen::class.java)
            startActivity(intent_trail_by_dist_bt)
        }

        //Trails by Type button
        val trail_by_type_bt = findViewById<Button>(R.id.trail_by_type_bt_discover_new_trail)
        trail_by_type_bt.setOnClickListener {
            val intent_trail_by_type_bt = Intent(this, TrailByTypeScreen::class.java)
            startActivity(intent_trail_by_type_bt)
        }
    }
}