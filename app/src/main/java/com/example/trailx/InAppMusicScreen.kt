@file:Suppress("DEPRECATION")

package com.example.trailx

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.BuildConfig
import java.util.*

class InAppMusicScreen : AppCompatActivity() {
    private lateinit var play: ImageView
    private lateinit var prev: ImageView
    private lateinit var next: ImageView
    private var imageView: ImageView? = null
    private var songTitle: TextView? = null
    //private var mSeekBarTime: SeekBar? = null
    private lateinit var mSeekBarVol: SeekBar
    // private val runnable: Runnable? = null
    private var mAudioManager: AudioManager? = null
    private var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_music_screen)
        val bar = this.supportActionBar
        bar?.hide()
        val back_to_home_bt_bar = findViewById<View>(R.id.back_to_home_bt_in_app_music) as Button
        back_to_home_bt_bar.setOnClickListener {
            val intent_back_to_home_bt_bar =
                Intent(this@InAppMusicScreen as Context, HomeScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<View>(R.id.settings_bt_in_app_music) as Button
        settings_bt_bar.setOnClickListener {
            val intent_settings_bt_bar =
                Intent(this@InAppMusicScreen as Context, SettingsScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar = findViewById<View>(R.id.discover_new_trails_bt_in_app_music) as Button
        discover_new_trails_bt_bar.setOnClickListener {
            val intent_discover_new_trails_bt_bar =
                Intent(this@InAppMusicScreen as Context, DiscoverNewTrailsScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<View>(R.id.active_trail_bt_in_app_music) as Button
        active_trail_bt_bar.setOnClickListener {
            val intent_active_trail_bt_bar =
                Intent(this@InAppMusicScreen as Context, ActiveTrailScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<View>(R.id.my_trails_bt_in_app_music) as Button
        my_trails_bt_bar.setOnClickListener {
            val intent_my_trails_bt_bar =
                Intent(this@InAppMusicScreen as Context, MyTrailsScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<View>(R.id.music_bt_in_app_music) as Button
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar =
                Intent(this@InAppMusicScreen as Context, MusicScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_music_bt_bar)
        }
        mAudioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        // initializing views
        play = findViewById(R.id.play_bt_in_app_music)
        prev = findViewById(R.id.prev_bt_in_app_music)
        next = findViewById(R.id.next_bt_in_app_music)
        songTitle = findViewById(R.id.songTitle_in_app_music)
        imageView = findViewById(R.id.album_cover_in_app_music)
        //mSeekBarTime = findViewById(R.id.seekBarTime_in_app_music)
        mSeekBarVol = findViewById(R.id.seekBarVol_in_app_music)

        // creating an ArrayList to store our songs
        val songs = ArrayList<Int>()
        songs.add(0, R.raw.alive)
        songs.add(1, R.raw.gurenge)
        songs.add(2, R.raw.sunflower)
        songs.add(3, R.raw.imready)
        songs.add(4, R.raw.homura)

        // intializing mediaplayer
        var mMediaPlayer = MediaPlayer.create(
            applicationContext,
            songs[currentIndex]
        )

        // seekbar volume
        val maxV = mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val curV = mAudioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        this.mSeekBarVol.max = maxV
        mSeekBarVol.progress = curV
        mSeekBarVol.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mAudioManager!!.setStreamVolume(
                    AudioManager.STREAM_MUSIC, progress,
                    0
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //above seekbar volume
        play.setOnClickListener {
            //with(mSeekBarTime) { this!!.max = mMediaPlayer.duration }
            if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.pause()
                play.setImageResource(R.drawable.pause_btn_player)
            } else {
                if (BuildConfig.DEBUG && mMediaPlayer == null) {
                    error("Assertion failed")
                }
                mMediaPlayer.start()
                play.setImageResource(R.drawable.pause_btn_player)
            }
            this.songNames()
        }
        next.setOnClickListener {
            if (mMediaPlayer != null) {
                play.setImageResource(R.drawable.pause_btn_player)
            }
            if (currentIndex < songs.size - 1) {
                currentIndex++
            } else {
                currentIndex = 0
            }
            if (BuildConfig.DEBUG && mMediaPlayer == null) {
                error("Assertion failed")
            }
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.stop()
            }
            mMediaPlayer = MediaPlayer.create(
                applicationContext,
                songs[currentIndex]
            )
            mMediaPlayer.start()
            songNames()
        }
        prev.setOnClickListener {
            if (mMediaPlayer != null) {
                play.setImageResource(R.drawable.pause_btn_player)
            }
            if (currentIndex > 0) {
                currentIndex--
            } else {
                currentIndex = songs.size - 1
            }
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.stop()
            }
            mMediaPlayer = MediaPlayer.create(
                applicationContext,
                songs[currentIndex]
            )
            mMediaPlayer.start()
            songNames()
        }
    }

    @Throws(NullPointerException::class)
    @SuppressLint("SetTextI18n")
    private fun songNames() {
        if (currentIndex == 0) {
            songTitle!!.text = "Alive"
            imageView!!.setImageResource(R.drawable.alive)
        }
        if (currentIndex == 1) {
            songTitle!!.text = "Gurenge"
            imageView!!.setImageResource(R.drawable.gurenge)
        }
        if (currentIndex == 2) {
            songTitle!!.text = "Sunflower"
            imageView!!.setImageResource(R.drawable.sunflower)
        }
        if (currentIndex == 3) {
            songTitle!!.text = "I'm Ready"
            imageView!!.setImageResource(R.drawable.imready)
        }
        if (currentIndex == 4) {
            songTitle!!.text = "Homura"
            imageView!!.setImageResource(R.drawable.homura)
        }
        // seekbar duration
        mMediaPlayer?.setOnPreparedListener {
            //mSeekBarTime?.max = mMediaPlayer!!.duration
            mMediaPlayer!!.start()
        }
        //mSeekBarTime?.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
        fun onProgressChanged(seekBar:SeekBar, progress:Int, fromUser:Boolean) {
                if (fromUser) {
                    mMediaPlayer?.seekTo(progress)
                    //mSeekBarTime?.progress = progress
                }
            }
            fun onStartTrackingTouch(seekBar:SeekBar) {}
            fun onStopTrackingTouch(seekBar:SeekBar) {
            }
        //})
        Thread {
            while (mMediaPlayer != null) {
                try {
                    if (mMediaPlayer!!.isPlaying) {
                        val message = Message()
                        message.what = mMediaPlayer!!.currentPosition
                        handler.sendMessage(message)
                        Thread.sleep(1000)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    @SuppressLint("Handler Leak")
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(@SuppressLint("HandlerLeak") msg: Message) {
            //mSeekBarTime!!.progress = msg.what
        }
    }

    companion object {
        var mMediaPlayer: MediaPlayer? = null
    }

}