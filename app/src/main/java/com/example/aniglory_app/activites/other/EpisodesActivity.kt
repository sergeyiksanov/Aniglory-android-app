//package com.example.aniglory_app.activites.other
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.aniglory_app.R
//import com.example.aniglory_app.activites.players.PlayerAnilibriaActivity
//import com.example.aniglory_app.adapters.EpisodesAdapter
//import com.example.aniglory_app.databinding.ActivityEpisodesBinding
//import com.example.aniglory_app.values.Constants
//import com.example.aniglory_app.values.Network
//import com.google.gson.JsonElement
//import io.ktor.client.call.body
//import io.ktor.client.request.parameter
//import io.ktor.client.request.request
//import io.ktor.client.request.url
//import io.ktor.http.ContentType
//import io.ktor.http.HttpMethod
//import io.ktor.http.HttpStatusCode
//import io.ktor.http.contentType
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.lang.Exception
//
//class EpisodesActivity : AppCompatActivity() {
//    lateinit var binding: ActivityEpisodesBinding
//    lateinit var layoutManager: LinearLayoutManager
//    private val adapter = EpisodesAdapter() {
//        val url = it.findViewById<TextView>(R.id.episodeUrl).text.toString()
//        startPlayer(url)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityEpisodesBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        var episodes = intent.getStringExtra(com.example.aniglory_app.values.Constants.EPISODES)
//        val code = intent.getStringExtra(com.example.aniglory_app.values.Constants.CODE)
//
//        layoutManager = LinearLayoutManager(this)
//        init()
//        getUrlEpisodes(code!!, episodes!!.toInt())
//    }
//
//    private fun init() = with(binding) {
//        rcView.layoutManager = layoutManager
//        rcView.adapter = adapter
//    }
//
//    private fun getUrlEpisodes(code: String, episodes: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try{
//                val response = Network.httpClient.request{
//                    url(Network.TITLE_URL)
//                    parameter("code", code)
//                    parameter("filter", "player")
//                    method = HttpMethod.Get
//                    contentType(ContentType.Application.Json)
//                }
//
//                if(response.status == HttpStatusCode.OK){
//                    val data: JsonElement = response.body()
//                    withContext(Dispatchers.Main) {
//                        Log.i("NETWORK_TITLE", data.asJsonObject["player"].asJsonObject["list"].asJsonObject["1"].asJsonObject["hls"].asJsonObject["fhd"].toString())
//
//                        val listEpisodes = mutableListOf<String>()
//
//                        Log.i("NETWORK_TITLE", episodes.toString())
//                        for(i: Int in 1..episodes) {
//                            Log.i("NETWORK_TITLE", i.toString() + "i")
//                            listEpisodes.add(i.toString() + ":" + data.asJsonObject["player"].asJsonObject["list"].asJsonObject[i.toString()].asJsonObject["hls"].asJsonObject["fhd"].toString())
//                        }
//
//                        Log.i("NETWORK_TITLE", listEpisodes.toString())
////                        adapter.createNewAdapter(listEpisodes)
//                    }
//                }
//            }
//            catch (err: Exception){
//                withContext(Dispatchers.Main){
//                    Log.i("NETWORK_TITLE", err.toString())
//                }
//            }
//        }
//    }
//
//    private fun startPlayer(url: String) {
//        val i = Intent(this, PlayerAnilibriaActivity::class.java)
//        i.putExtra(Constants.PLAYER_URL, (Network.PLAYER_ANILIBRIA_HOST + url).replace("\"", ""))
//        startActivity(i)
//    }
//}