package com.example.aniglory_app.activites.other

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.ActivityTitlesBinding
import com.example.aniglory_app.models.anilibria.TitlesModel
import com.example.aniglory_app.models.anilibria.listModel
import com.example.aniglory_app.values.Network
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

class TitlesActivity : AppCompatActivity() {
    lateinit var binding: ActivityTitlesBinding
    lateinit var layoutManager: GridLayoutManager
    private val adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
        startEpisodes(id)
    }
    var isLoading = false
    var num_request = 0
    var type_request = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTitlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = GridLayoutManager(this, 3)

        init()
        baseTitles("create", num_request)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(binding.searchView.query.isEmpty()){
                    baseTitles("create",0)
                    type_request = "base"
                }
                else{
                    type_request = "search"
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try{
                            val response = Network.httpClient.request{
                                url(Network.SEARCH_URL)
                                parameter("search", p0)
                                parameter("filter", "id,code,names,posters")
                                method = HttpMethod.Get
                                contentType(ContentType.Application.Json)
                            }

                            if(response.status == HttpStatusCode.OK){
                                val data: JsonElement = response.body()
                                val titles: MutableList<listModel> = Gson().fromJson(data, TitlesModel::class.java).list
                                withContext(Dispatchers.Main) {
                                    Log.i("NETWORK_TITLE", titles.toString())
//                                    adapter.createNewAdapter(titles)
                                }
                            }
                        }
                        catch (err: Exception){
                            withContext(Dispatchers.Main){
                                Log.i("NETWORK_TITLE", err.toString())
                            }
                        }
                    }
                    isLoading = false
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(binding.searchView.query.isEmpty()){
                    baseTitles("create", 0)
                    type_request = "base"
                }
                else{
                    type_request = "search"
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try{
                            val response = Network.httpClient.request{
                                url(Network.SEARCH_URL)
                                parameter("search", p0)
                                parameter("filter", "id,code,names,posters")
                                method = HttpMethod.Get
                                contentType(ContentType.Application.Json)
                            }

                            if(response.status == HttpStatusCode.OK){
                                val data: JsonElement = response.body()
                                val titles: MutableList<listModel> = Gson().fromJson(data, TitlesModel::class.java).list
                                withContext(Dispatchers.Main) {
                                    Log.i("NETWORK_TITLE", titles.toString())
//                                    adapter.createNewAdapter(titles)
                                }
                            }
                        }
                        catch (err: Exception){
                            withContext(Dispatchers.Main){
                                Log.i("NETWORK_TITLE", err.toString())
                            }
                        }
                    }
                    isLoading = false
                }

                return true
            }

        })

        binding.bBookmark.setOnClickListener { openBookmarks() }
        binding.bProfile.setOnClickListener { openProfile() }
        binding.bSettings.setOnClickListener {  }

        binding.rcView.addOnScrollListener(object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(type_request == "base") {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if(!isLoading) {
                        if( (visibleItemCount+firstVisibleItems) >= totalItemCount) {
                            num_request += 1
                            baseTitles("update", num_request)
                        }
                    }
                }
            }
        })
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    private fun startEpisodes(id: String){
        val i = Intent(this, TitleActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }

    private fun baseTitles(type: String, num_request: Int) {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.TITLES_URL)
                    parameter("limit", "9")
                    parameter("filter", "id,code,names,posters")
                    if(num_request != 0){
                        parameter("after", (num_request * 9).toString())
                    }
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    val titles: MutableList<listModel> = Gson().fromJson(data, TitlesModel::class.java).list
                    withContext(Dispatchers.Main) {
                        Log.i("NETWORK_TITLE", titles.toString())
                        if(type == "update"){
//                            adapter.updateAdapter(titles)
                        }
                        else if(type == "create"){
//                            adapter.createNewAdapter(titles)
                        }
                    }
                }
            }
            catch (err: Exception){
                withContext(Dispatchers.Main){
                    Log.i("NETWORK_TITLE", err.toString())
                }
            }
        }
        isLoading = false
    }

    private fun openProfile() {
        val i = Intent(this, ProfileActivity::class.java)
        startActivity(i)
    }

    private fun openBookmarks() {
        val i = Intent(this, BookmarksActivity::class.java)
        startActivity(i)
    }
}