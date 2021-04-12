@file:Suppress("DEPRECATION")

package com.example.trailx

//Necessary imports
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
    //Necessary Variables for In - App Music
    private lateinit var play: ImageView
    private lateinit var prev: ImageView
    private lateinit var next: ImageView
    private var imageView: ImageView? = null
    private var songTitle: TextView? = null
    private lateinit var mSeekBarVol: SeekBar
    private var mAudioManager: AudioManager? = null
    private var currentIndex = 0

    //Function that is invoked on the creation of the Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_music_screen)
        val bar = this.supportActionBar
        bar?.hide()

        //Home button
        val back_to_home_bt_bar = findViewById<View>(R.id.back_to_home_bt_in_app_music) as Button
        back_to_home_bt_bar.setOnClickListener {
            val intent_back_to_home_bt_bar =
                Intent(this@InAppMusicScreen as Context, HomeScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_back_to_home_bt_bar)
        }

        //Settings button
        val settings_bt_bar = findViewById<View>(R.id.settings_bt_in_app_music) as Button
        settings_bt_bar.setOnClickListener {
            val intent_settings_bt_bar =
                Intent(this@InAppMusicScreen as Context, SettingsScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_settings_bt_bar)
        }

        //Discover New Trails button
        val discover_new_trails_bt_bar = findViewById<View>(R.id.discover_new_trails_bt_in_app_music) as Button
        discover_new_trails_bt_bar.setOnClickListener {
            val intent_discover_new_trails_bt_bar =
                Intent(this@InAppMusicScreen as Context, DiscoverNewTrailsScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_discover_new_trails_bt_bar)
        }

        //Active trails button
        val active_trail_bt_bar = findViewById<View>(R.id.active_trail_bt_in_app_music) as Button
        active_trail_bt_bar.setOnClickListener {
            val intent_active_trail_bt_bar =
                Intent(this@InAppMusicScreen as Context, ActiveTrailScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_active_trail_bt_bar)
        }

        //My Trails button
        val my_trails_bt_bar = findViewById<View>(R.id.my_trails_bt_in_app_music) as Button
        my_trails_bt_bar.setOnClickListener {
            val intent_my_trails_bt_bar =
                Intent(this@InAppMusicScreen as Context, MyTrailsScreen::class.java)
            this@InAppMusicScreen.startActivity(intent_my_trails_bt_bar)
        }

        //Music button
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
        mSeekBarVol = findViewById(R.id.seekBarVol_in_app_music)

        // creating an ArrayList to store our songs
        val songs = ArrayList<Int>()
        songs.add(0, R.raw.alive)
        songs.add(1, R.raw.gurenge)
        songs.add(2, R.raw.sunflower)
        songs.add(3, R.raw.imready)
        songs.add(4, R.raw.homura)
        songs.add(5, R.raw.aaomilochalen)
        songs.add(6, R.raw.anpanman)
        songs.add(7, R.raw.aotop1)
        songs.add(8, R.raw.blindinglights)
        songs.add(9, R.raw.bloodsweattears)
        songs.add(10, R.raw.dynamite)
        songs.add(11, R.raw.falsegod)
        songs.add(12, R.raw.friends)
        songs.add(13, R.raw.golden)
        songs.add(14, R.raw.ificanthaveyou)
        songs.add(15, R.raw.illegalweapon)
        songs.add(16, R.raw.illstay)
        songs.add(17, R.raw.namonamo)
        songs.add(18, R.raw.pillowtalk)
        songs.add(19, R.raw.shinzouwosasageyo)
        songs.add(20, R.raw.telepathy)
        songs.add(21, R.raw.treatyoubetter)
        songs.add(22, R.raw.tumse)
        songs.add(23, R.raw.wonder)


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

        //Moving to the next song
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

        //Moving to the previous song
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

    //Function to display the song names
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
        if (currentIndex == 5) {
            songTitle!!.text = "Aao Milo Chalo"
            imageView!!.setImageResource(R.drawable.aaomilochalen)
        }
        if (currentIndex == 6) {
            songTitle!!.text = "Anpanman"
            imageView!!.setImageResource(R.drawable.anpanman)
        }
        if (currentIndex == 7) {
            songTitle!!.text = "Guren No Yumiya"
            imageView!!.setImageResource(R.drawable.aotop1)
        }
        if (currentIndex == 8) {
            songTitle!!.text = "Blinding Lights"
            imageView!!.setImageResource(R.drawable.blindinglights)
        }
        if (currentIndex == 9) {
            songTitle!!.text = "Blood, Sweat and Tears"
            imageView!!.setImageResource(R.drawable.bloodsweattears)
        }
        if (currentIndex == 10) {
            songTitle!!.text = "Dynamite"
            imageView!!.setImageResource(R.drawable.dynamite)
        }
        if (currentIndex == 11) {
            songTitle!!.text = "False God"
            imageView!!.setImageResource(R.drawable.falsegod)
        }
        if (currentIndex == 12) {
            songTitle!!.text = "FRIENDS"
            imageView!!.setImageResource(R.drawable.friends)
        }
        if (currentIndex == 13) {
            songTitle!!.text = "Golden"
            imageView!!.setImageResource(R.drawable.golden)
        }
        if (currentIndex == 14) {
            songTitle!!.text = "If I can't have you"
            imageView!!.setImageResource(R.drawable.ificanthaveyou)
        }
        if (currentIndex == 15) {
            songTitle!!.text = "Illegal Weapon 2.0"
            imageView!!.setImageResource(R.drawable.illegalweapon)
        }
        if (currentIndex == 16) {
            songTitle!!.text = "I'll Stay"
            imageView!!.setImageResource(R.drawable.illstay)
        }
        if (currentIndex == 17) {
            songTitle!!.text = "Namo Namo"
            imageView!!.setImageResource(R.drawable.namonamo)
        }
        if (currentIndex == 18) {
            songTitle!!.text = "Pillowtalk"
            imageView!!.setImageResource(R.drawable.pillowtalk)
        }
        if (currentIndex == 19) {
            songTitle!!.text = "Shinzou Wo Sasageyo"
            imageView!!.setImageResource(R.drawable.shinzouwosasageyo)
        }
        if (currentIndex == 20) {
            songTitle!!.text = "Telepathy"
            imageView!!.setImageResource(R.drawable.telepathy)
        }
        if (currentIndex == 21) {
            songTitle!!.text = "Treat You Better"
            imageView!!.setImageResource(R.drawable.treatyoubetter)
        }
        if (currentIndex == 22) {
            songTitle!!.text = "Tum Se"
            imageView!!.setImageResource(R.drawable.tumse)
        }
        if (currentIndex == 23) {
            songTitle!!.text = "Wonder"
            imageView!!.setImageResource(R.drawable.wonder)
        }
        // seekbar duration
        mMediaPlayer?.setOnPreparedListener {
            mMediaPlayer!!.start()
        }

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