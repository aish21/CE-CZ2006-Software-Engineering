package com.example.trailx

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_trail_by_distance_screen.*



class TrailByDistanceScreen : AppCompatActivity() {

    private lateinit var dist_value: EditText
    private lateinit var dist_textDisplay:TextView
    private lateinit var name_textDisplay:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_by_distance_screen)

        dist_value = findViewById<EditText>(R.id.trail_by_dist_value_trails_by_dist)
        dist_textDisplay = findViewById<TextView>(R.id.trail_distance_trail_by_distance)
        name_textDisplay = findViewById<TextView>(R.id.name_trail_by_distance)

        val search_trails_by_dist_bt = findViewById<Button>(R.id.dist_search_bt_trails_by_dist)
        search_trails_by_dist_bt.setOnClickListener{
            val distance_searched = dist_value.text.toString()
            if(dist_check()){
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
            }
        }

        val start_trail_bt_dist = findViewById<Button>(R.id.start_trail_trails_by_dist)
        start_trail_bt_dist.setOnClickListener{
            if(dist_check()){
                startActivity(Intent(this@TrailByDistanceScreen, ActiveTrailScreen::class.java))
            }
        }

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
            val intent_discover_new_trails_bt_bar = Intent(
                this,
                DiscoverNewTrailsScreen::class.java
            )
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

    @SuppressLint("SetTextI18n")
    private fun dist_check():Boolean {
        var check_condition = false
        val distance_searched = dist_value.text.toString()
        val final_distance: Int = distance_searched.toInt()

        if(distance_searched.isEmpty())
            Toast.makeText(this, "Invalid Input! Try Again.", Toast.LENGTH_LONG).show()
        else {
            if(final_distance <= 1){
                name_textDisplay.text = "Telok Ayer Park Trail"
                dist_textDisplay.text = "460m"
                Toast.makeText(this, "Trail found!", Toast.LENGTH_SHORT).show()
                global.choice = 1
                check_condition = true

            } else if(final_distance in 2..5){
                name_textDisplay.text = "Bukit Batok Trail"
                dist_textDisplay.text = "2km"
                Toast.makeText(this, "Trail found!", Toast.LENGTH_SHORT).show()
                global.choice = 2
                check_condition = true
            }else if(final_distance in 6..10){
                name_textDisplay.text = "Bedok Reservoir Loop Trail"
                dist_textDisplay.text = "7km"
                Toast.makeText(this, "Trail found!", Toast.LENGTH_SHORT).show()
                global.choice = 3
                check_condition = true
            } else {
                Toast.makeText(this, "No trails found with the specified distance! Try another value!", Toast.LENGTH_LONG).show()
            }
        }
        return check_condition
    }
}

