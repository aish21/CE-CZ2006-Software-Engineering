package com.example.trailx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_active_trail_screen.*
import kotlinx.android.synthetic.main.activity_active_trail_screen.mapView_active_trail
import kotlinx.android.synthetic.main.activity_trail_by_type_screen.*

class TrailByTypeScreen : AppCompatActivity() {
    private lateinit var type_textDisplay: TextView
    private lateinit var name_textDisplay: TextView
    var check_trail:Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_by_type_screen)
        supportActionBar?.hide()

        type_textDisplay = findViewById<TextView>(R.id.trail_type_trail_by_type)
        name_textDisplay = findViewById<TextView>(R.id.name_trail_by_type)

        val nature_bt = findViewById<Button>(R.id.nature_trail_bt_type_trail)
        nature_bt.setOnClickListener{
            name_textDisplay.text = "Bedok Reservoir Loop Trail"
            type_textDisplay.text = "Nature Trail"
            Toast.makeText(this, "Trail found!", Toast.LENGTH_SHORT).show()
            global.choice = 2
            check_trail = true
        }

        val city_bt = findViewById<Button>(R.id.city_trail_bt_type_trail)
        city_bt.setOnClickListener{
            name_textDisplay.text = "Telok Ayer Park Trail"
            type_textDisplay.text = "City Trail"
            Toast.makeText(this, "Trail found!", Toast.LENGTH_SHORT).show()
            global.choice = 1
            check_trail = true
        }

        val start_trail_bt_type = findViewById<Button>(R.id.start_trail_trails_by_type)
        start_trail_bt_type.setOnClickListener{
            if(check_trail){
                startActivity(Intent(this@TrailByTypeScreen, ActiveTrailScreen::class.java))
            }
        }

        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_trails_by_type)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_trails_by_type)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_trails_by_type)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_trails_by_type)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_trails_by_type)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_trails_by_type)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }
    }
}