package com.example.aniglory_app.activites.other

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.ActivityBookmarksBinding
import com.example.aniglory_app.models.anilibria.listModel
import com.example.aniglory_app.values.Network
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BookmarksActivity : AppCompatActivity() {
    var current_bookmark = "watching"
    lateinit var binding: ActivityBookmarksBinding

    val color_accent = R.color.accent
    val color_default = R.color.white

    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private val adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
        startEpisodes(id)
    }
    lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookmarksBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.bSearch.setOnClickListener { openSearch() }
        binding.bProfile.setOnClickListener { openProfile() }
        binding.bSettings.setOnClickListener {  }

        auth = Firebase.auth

        layoutManager = GridLayoutManager(this, 3)

        init()
        getData()

        binding.bWatching.setOnClickListener {
            binding.bWatching.setBackgroundResource(color_accent)
            binding.bWatched.setBackgroundResource(color_default)
            binding.bPlans.setBackgroundResource(color_default)
            binding.bPostponed.setBackgroundResource(color_default)
            binding.bAbandoned.setBackgroundResource(color_default)

            binding.bWatching.cardElevation = 5F
            binding.bWatched.cardElevation = 5F
            binding.bPlans.cardElevation = 5F
            binding.bPostponed.cardElevation = 5F
            binding.bAbandoned.cardElevation = 5F

            binding.bWatching.radius = 10F
            binding.bWatched.radius = 10F
            binding.bPlans.radius = 10F
            binding.bPostponed.radius = 10F
            binding.bAbandoned.radius = 10F

            binding.tWatching.setTextColor(Color.WHITE)
            binding.tWatched.setTextColor(Color.BLACK)
            binding.tPlans.setTextColor(Color.BLACK)
            binding.tPostponed.setTextColor(Color.BLACK)
            binding.tAbandoned.setTextColor(Color.BLACK)

            var data: MutableList<listModel> = mutableListOf()
//            adapter.createNewAdapter(data)
            current_bookmark = "watching"
            getData()
        }
        binding.bWatched.setOnClickListener {
            binding.bWatching.setBackgroundResource(color_default)
            binding.bWatched.setBackgroundResource(color_accent)
            binding.bPlans.setBackgroundResource(color_default)
            binding.bPostponed.setBackgroundResource(color_default)
            binding.bAbandoned.setBackgroundResource(color_default)

            binding.bWatching.cardElevation = 5F
            binding.bWatched.cardElevation = 5F
            binding.bPlans.cardElevation = 5F
            binding.bPostponed.cardElevation = 5F
            binding.bAbandoned.cardElevation = 5F

            binding.bWatching.radius = 10F
            binding.bWatched.radius = 10F
            binding.bPlans.radius = 10F
            binding.bPostponed.radius = 10F
            binding.bAbandoned.radius = 10F

            binding.tWatching.setTextColor(Color.BLACK)
            binding.tWatched.setTextColor(Color.WHITE)
            binding.tPlans.setTextColor(Color.BLACK)
            binding.tPostponed.setTextColor(Color.BLACK)
            binding.tAbandoned.setTextColor(Color.BLACK)

            var data: MutableList<listModel> = mutableListOf()
//            adapter.createNewAdapter(data)
            current_bookmark = "watched"
            getData()
        }
        binding.bPlans.setOnClickListener {
            binding.bWatching.setBackgroundResource(color_default)
            binding.bWatched.setBackgroundResource(color_default)
            binding.bPlans.setBackgroundResource(color_accent)
            binding.bPostponed.setBackgroundResource(color_default)
            binding.bAbandoned.setBackgroundResource(color_default)

            binding.bWatching.cardElevation = 5F
            binding.bWatched.cardElevation = 5F
            binding.bPlans.cardElevation = 5F
            binding.bPostponed.cardElevation = 5F
            binding.bAbandoned.cardElevation = 5F

            binding.bWatching.radius = 10F
            binding.bWatched.radius = 10F
            binding.bPlans.radius = 10F
            binding.bPostponed.radius = 10F
            binding.bAbandoned.radius = 10F

            binding.tWatching.setTextColor(Color.BLACK)
            binding.tWatched.setTextColor(Color.BLACK)
            binding.tPlans.setTextColor(Color.WHITE)
            binding.tPostponed.setTextColor(Color.BLACK)
            binding.tAbandoned.setTextColor(Color.BLACK)

            var data: MutableList<listModel> = mutableListOf()
//            adapter.createNewAdapter(data)
            current_bookmark = "plans"
            getData()
        }
        binding.bPostponed.setOnClickListener {
            binding.bWatching.setBackgroundResource(color_default)
            binding.bWatched.setBackgroundResource(color_default)
            binding.bPlans.setBackgroundResource(color_default)
            binding.bPostponed.setBackgroundResource(color_accent)
            binding.bAbandoned.setBackgroundResource(color_default)

            binding.bWatching.cardElevation = 5F
            binding.bWatched.cardElevation = 5F
            binding.bPlans.cardElevation = 5F
            binding.bPostponed.cardElevation = 5F
            binding.bAbandoned.cardElevation = 5F

            binding.bWatching.radius = 10F
            binding.bWatched.radius = 10F
            binding.bPlans.radius = 10F
            binding.bPostponed.radius = 10F
            binding.bAbandoned.radius = 10F

            binding.tWatching.setTextColor(Color.BLACK)
            binding.tWatched.setTextColor(Color.BLACK)
            binding.tPlans.setTextColor(Color.BLACK)
            binding.tPostponed.setTextColor(Color.WHITE)
            binding.tAbandoned.setTextColor(Color.BLACK)

            var data: MutableList<listModel> = mutableListOf()
//            adapter.createNewAdapter(data)
            current_bookmark = "postponed"
            getData()
        }
        binding.bAbandoned.setOnClickListener {
            binding.bWatching.setBackgroundResource(color_default)
            binding.bWatched.setBackgroundResource(color_default)
            binding.bPlans.setBackgroundResource(color_default)
            binding.bPostponed.setBackgroundResource(color_default)
            binding.bAbandoned.setBackgroundResource(color_accent)

            binding.bWatching.cardElevation = 5F
            binding.bWatched.cardElevation = 5F
            binding.bPlans.cardElevation = 5F
            binding.bPostponed.cardElevation = 5F
            binding.bAbandoned.cardElevation = 5F

            binding.bWatching.radius = 10F
            binding.bWatched.radius = 10F
            binding.bPlans.radius = 10F
            binding.bPostponed.radius = 10F
            binding.bAbandoned.radius = 10F

            binding.tWatching.setTextColor(Color.BLACK)
            binding.tWatched.setTextColor(Color.BLACK)
            binding.tPlans.setTextColor(Color.BLACK)
            binding.tPostponed.setTextColor(Color.BLACK)
            binding.tAbandoned.setTextColor(Color.WHITE)

            var data: MutableList<listModel> = mutableListOf()
//            adapter.createNewAdapter(data)
            current_bookmark = "abandoned"
            getData()
        }
    }

    private fun openSearch() {
        val i = Intent(this, TitlesActivity::class.java)
        startActivity(i)
    }

    private fun openProfile() {
        val i = Intent(this, ProfileActivity::class.java)
        startActivity(i)
    }

    private fun baseTitles(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.TITLE_URL)
                    parameter("code", id)
                    parameter("filter", "id,code,names,posters")
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    val titles = listOf(Gson().fromJson(data, listModel::class.java))
                    withContext(Dispatchers.Main) {
                        Log.i("NETWORK_TITLE", titles.toString())

//                        adapter.updateAdapter(titles)
                    }
                }
            }
            catch (err: Exception){
                withContext(Dispatchers.Main){
                    Log.i("NETWORK_TITLE", err.toString())
                }
            }
        }
    }

    private fun getData() {
        val docRef = db.collection("users").document(auth.currentUser!!.uid)

        docRef.get()
            .addOnSuccessListener { document ->
                if(document != null) {
                    if(document.data!!["bookmarks:" + current_bookmark] != null) {
                        val id_list = document.data!!["bookmarks:" + current_bookmark] as List<String>
                        for(it1 in id_list) {
                            baseTitles(it1)
                            Log.i("NETWORK_TITLE", it1)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.i("NETWORK", e.toString())
            }
    }

    private fun startEpisodes(id: String){
        val i = Intent(this, TitleActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }
}