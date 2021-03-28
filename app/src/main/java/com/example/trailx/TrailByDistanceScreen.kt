package com.example.trailx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_active_trail_screen.*
import kotlinx.android.synthetic.main.activity_active_trail_screen.mapView_active_trail
import kotlinx.android.synthetic.main.activity_trail_by_distance_screen.*

class TrailByDistanceScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_trail_by_distance_screen)

        supportActionBar?.hide()
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token))

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_trail_by_distance_screen)
        mapView_trails_by_dist?.onCreate(savedInstanceState)
        mapView_trails_by_dist?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                Style.Builder().fromUri("mapbox://styles/ashsongh/ckmt73aky3hpc17l93d4rwbbt")
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
            }
        }
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

    // Add the mapView lifecycle to the activity's lifecycle methods
    public override fun onResume() {
        super.onResume()
        mapView_active_trail?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView_active_trail?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView_active_trail?.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView_active_trail?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView_active_trail?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView_active_trail?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView_active_trail?.onSaveInstanceState(outState)
    }
}