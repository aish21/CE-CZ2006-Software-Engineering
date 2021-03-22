package com.example.trailx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote


class MusicScreen : AppCompatActivity() {
    private val CLIENT_ID = "6945fbec77614c7fb910a4baf1358056"
    private val REDIRECT_URI = "http://671065406800-4rtd4b0cdqntnpak5mc0ftov1q3fjla9.apps.googleusercontent.com"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_screen)

        supportActionBar?.hide()
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

    protected override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MusicScreen", "Connected! Yay!")

                    // Now you can start interacting with App Remote
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MusicScreen", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }
    private fun connected() {
        // Then we will write some more code here.
        mSpotifyAppRemote?.getPlayerApi()?.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
    }
    protected override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}