package com.example.aniglory_app.activites.players

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.ActivityPlayerAnilibriaBinding
import com.example.aniglory_app.values.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class PlayerAnilibriaActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayerAnilibriaBinding
    lateinit var exoPlayer: ExoPlayer
    val db = Firebase.firestore
    lateinit var auth: FirebaseAuth
    companion object {
        var isFullScreen = false
        var isLock = false
    }
    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var bt_fullscreen: ImageView
    var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerAnilibriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        startTime = System.currentTimeMillis()

        val uri = intent.getStringExtra("http//kodik.info/serial/19248/803944eb832adacd4d4bec7d4221f941/720p?translations=false")

        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)

        bt_fullscreen.setOnClickListener {
            if(!isFullScreen) {
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.baseline_fullscreen_exit
                ))
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            else {
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.baseline_fullscreen
                ))
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            isFullScreen = !isFullScreen
        }

        bt_lockscreen.setOnClickListener {
            if(!isLock) {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.baseline_lock
                ))

            }
            else {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.baseline_lock_open
                ))
            }

            isLock = !isLock
            lockScreen(isLock)
        }

        simpleExoPlayer = SimpleExoPlayer.Builder(this).setSeekBackIncrementMs(5000).setSeekForwardIncrementMs(5000).build()
        binding.player.player = simpleExoPlayer
        binding.player.keepScreenOn = true
        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                if(playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar2.visibility = View.VISIBLE
                }
                else if(playbackState == Player.STATE_READY) {
                    binding.progressBar2.visibility = View.GONE
                }
            }
        })

        val videoSource = Uri.parse(uri)
        val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

//    private fun checkAuthState(): Boolean {
//        return auth.currentUser != null
//    }

    private fun lockScreen(lock: Boolean) {
        val sec_mid = findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom = findViewById<LinearLayout>(R.id.sec_controlvid2)

        if(lock) {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        }
        else {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if(isLock) return
        if(resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            bt_fullscreen.performClick()
        }

        else super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()

//        val totalTime: Long = System.currentTimeMillis() - startTime
//        val cal = Calendar.getInstance()
//        cal.timeInMillis = totalTime
//
//        if(checkAuthState()) {
//            updOtherData(cal)
//        }

        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()

        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.pause()
    }
//
//    private fun updOtherData(time: Calendar) {
//        val data = hashMapOf(
//            "viewing time: day" to time.get(Calendar.HOUR) / 24,
//            "viewing time: hour" to time.get(Calendar.HOUR),
//            "viewing time: minute" to time.get(Calendar.MINUTE),
//            "viewing time: second" to time.get(Calendar.SECOND)
//        )
//
//        db.collection("users").document(auth.currentUser!!.uid).set(data)
//    }
}