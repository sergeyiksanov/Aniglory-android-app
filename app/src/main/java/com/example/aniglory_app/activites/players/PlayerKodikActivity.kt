package com.example.aniglory_app.activites.players

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.aniglory_app.databinding.ActivityPlayerKodikBinding
import com.example.aniglory_app.for_player.JavaInterface
import com.example.aniglory_app.for_player.NewWebChromeClient
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.values.Constants

class PlayerKodikActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayerKodikBinding

    private var ji = JavaInterface()

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerKodikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url = intent.getStringExtra(Constants.PLAYER_URL).toString()
        var start_time = intent.getIntExtra("start_time", 0)
        Log.i("URL", "http:" + url)

        ji.setStartTime(start_time)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val html: String = """
            <body style="margin: 0; padding: 0">
                <iframe id="kodik-player" src=${"http:" + url + "?start_from=$start_time"} width="100%" height="100%" frameborder="0" top="0" left="0" allowfullscreen allow="autoplay *; fullscreen *"></iframe>
                <script type="text/javascript">
                    function kodikMessageListener(message) {
                        if (message.data.key == 'kodik_player_time_update') {
                            test.getCurrentTime(message.data.value);
                        }
                        if (message.data.key == 'kodik_player_duration_update') {
                            test.getFullTime(Math.trunc(message.data.value));
                        }
                    }

                    

                    if (window.addEventListener) {
                        window.addEventListener('message', kodikMessageListener);
                    } else {
                        window.attachEvent('onmessage', kodikMessageListener);
                    }
                </script>    
            </body>""".trimMargin()

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(ji, "test")
        binding.webView.webChromeClient = NewWebChromeClient(binding.webView, binding.fullscreenContainer, actvity = this)
        binding.webView.loadData(html, "text/html", "utf-8")
    }

    private fun saveData() {
        val start_time = ji.getCurrTime()
        val progress = ji.getProgress()
        Log.i("TEST_JS", progress.toString())
        val sharedPreferences = getSharedPreferences("last_watching_title", Context.MODE_MULTI_PROCESS)
        val editor = sharedPreferences?.edit()
////        editor?.remove("progress")
//        editor?.clear()
        if(Data.watching_name != null) {
            editor?.putString("name", Data.watching_name)
        }
        if(Data.watching_poster != null) {
            editor?.putString("poster", Data.watching_poster)
        }
        if(Data.watching_episode != null) {
            editor?.putString("episode", Data.watching_episode)
        }
        editor?.putInt("start_time", start_time)
        editor?.putString("url", url)
        editor?.putInt("progress", progress)
        editor?.commit()

        Log.i("TEST_JS", "data is saved")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TEST_JS", "onDestroy")

        saveData()
    }

    override fun onStop() {
        super.onStop()
        Log.i("TEST_JS", "onPause")

        saveData()
        Log.i("TEST_JS", "saveData вызван")
    }
}