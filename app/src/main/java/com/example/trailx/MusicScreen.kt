package com.example.trailx

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.*
import kotlinx.android.synthetic.main.activity_music_screen.*


class MusicScreen : AppCompatActivity() {

    private lateinit var YTMusic:Button
    private lateinit var youtubePlayer:YouTubePlayerView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_screen)

        YTMusic = findViewById<Button>(R.id.yt_music_bt_music)
        supportActionBar?.hide()

        YTMusic.setOnClickListener{
            webview_player_view.settings.javaScriptEnabled = true
            webview_player_view.loadUrl("https://www.youtube.com/watch?v=B-VJ6CQ76Gk")
        }

        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_music)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_music)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_music)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(
                this,
                DiscoverNewTrailsScreen::class.java
            )
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_music)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_music)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_music)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }
        val in_app_music_bt = findViewById<Button>(R.id.in_app_music_bt_music)
        in_app_music_bt.setOnClickListener {
            val intent_in_app_music_bt = Intent(this, InAppMusicScreen::class.java)
            startActivity(intent_in_app_music_bt)
        }
    }
}