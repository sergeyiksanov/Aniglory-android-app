package com.example.aniglory_app.activites.other

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.ActivityTitleBinding
import com.example.aniglory_app.models.anilibria.KodikPlayerModel
import com.example.aniglory_app.models.anilibria.TitleModel
import com.example.aniglory_app.values.Constants
import com.example.aniglory_app.values.Network
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

class TitleActivity : AppCompatActivity() {
    lateinit var binding: ActivityTitleBinding
    lateinit var title: TitleModel
    lateinit var dialog: Dialog
    lateinit var title_orig: String
    lateinit var episodes_kodik: JsonElement
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTitleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomSheetBehavior.from(binding.sheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        dialog = Dialog(this)
        auth = Firebase.auth

//        binding.addBookmarks.setOnClickListener { showCustomDialog2() }

        val id = intent.getStringExtra("id")

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.TITLE_URL)
                    parameter("code", id)
                    parameter("remove", "torrents")
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    title = Gson().fromJson(data, TitleModel::class.java)
                    withContext(Dispatchers.Main) {
                        Log.i("NETWORK_TITLE", title.toString())
                        binding.backgroundPoster.load(Network.IMAGE_HOST + title.posters.original.url)
                        binding.poster.load(Network.IMAGE_HOST + title.posters.original.url)

                        binding.title.text = title.names.ru
                        binding.description.text = title.description
                        binding.descriptionFull.text = title.description
//                        binding.originalTitle.text = "Оригинальное название - " + title.names.en
//                        title_orig = title.names.en
                        binding.season.text =  title.season.year.toString() + " " + title.season.string
//                        if(title.type.episodes != null) binding.countEpisodes.text = "Количество эпизодов - " + title.type.episodes.toString()
//                        else binding.countEpisodes.text = "Количество эпизодов - 1"
//                        binding.genres.text = "Жанры - " + title.genres.toString().replace("[", "").replace("]", "")
                        binding.bWatch.setOnClickListener { bWatch() }
                        binding.readFull.setOnClickListener { bRead() }
                        binding.close.setOnClickListener { bClose() }
                        binding.closeFull.setOnClickListener { finish() }
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

    private fun bWatch() {
        showCustomDialog()
    }

    private fun bRead() {
        binding.posters.visibility = View.GONE
        binding.sheet.visibility = View.GONE
        binding.descriptLayout.visibility = View.VISIBLE
    }

    private fun bClose() {
        binding.posters.visibility = View.VISIBLE
        binding.sheet.visibility = View.VISIBLE
        binding.descriptLayout.visibility = View.GONE
    }

    private fun b_kodik() {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.KODIK_PLAYER)
                    parameter("token", Network.API_TOKEN)
                    parameter("title_orig", title_orig)
                    parameter("full_match", "true")
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    val title_kodik = Gson().fromJson(data, KodikPlayerModel::class.java).results
                    withContext(Dispatchers.Main) {
                        Log.i("NETWORK_TITLE", title_kodik[0].seasons.toString())
                        episodes_kodik = title_kodik[0].seasons
                    }
                }
            }
            catch (err: Exception){
                withContext(Dispatchers.Main){
                    Log.i("NETWORK_TITLE", err.toString())
                }
            }
        }

//        val i = Intent(this, EpisodesActivity::class.java)
//        i.putExtra(Constants.PLAYER, Constants.KODIK)
//        i.putExtra(Constants.EPISODES_KODIK, episodes_kodik.toString())
    }

    private fun b_anilibria() {
//        val i = Intent(this, EpisodesActivity::class.java)
//        i.putExtra(Constants.PLAYER, Constants.ANILIBRIA)
//        i.putExtra(Constants.EPISODES, title.player.episodes.last.toString())
//        i.putExtra(Constants.CODE, title.code)
//        startActivity(i)
    }

    private fun showCustomDialog() {
        dialog.setContentView(R.layout.player_selection)
        val kodik: Button = dialog.findViewById(R.id.kodik)
        val anilibria: Button = dialog.findViewById(R.id.anilibria)

        kodik.setOnClickListener { b_kodik() }
        anilibria.setOnClickListener { b_anilibria() }

        dialog.show()
    }

    private fun showCustomDialog2() {
        dialog.setContentView(R.layout.bookmarks_selection)

        val watching: Button = dialog.findViewById(R.id.watching)
        val watched: Button = dialog.findViewById(R.id.watched)
        val plane: Button = dialog.findViewById(R.id.plane)
        val postponed: Button = dialog.findViewById(R.id.postponed)
        val abandoned: Button = dialog.findViewById(R.id.abandoned)

        watching.setOnClickListener { addBookmarks("watching") }
        watched.setOnClickListener { addBookmarks("watched") }
        plane.setOnClickListener { addBookmarks("plane") }
        postponed.setOnClickListener { addBookmarks("postponed") }
        abandoned.setOnClickListener { addBookmarks("abandoned") }

        dialog.show()
    }

    private fun addBookmarks(type: String) {
        val docRef = db.collection("users").document(auth.currentUser!!.uid)

        docRef.get()
            .addOnCompleteListener { document ->
                if(document != null) {
                    if(document.result.data!!["bookmarks:$type"] != null) {
                        val id_list = document.result.data!!["bookmarks:$type"] as MutableList<String>
                        id_list.add(title.code)

                        db.collection("users").document(auth.currentUser!!.uid).update("bookmarks:$type", id_list)
                    }
                    else {
                        db.collection("users").document(auth.currentUser!!.uid).update("bookmarks:$type", listOf(title.code))
                    }
                }
            }




    }
}