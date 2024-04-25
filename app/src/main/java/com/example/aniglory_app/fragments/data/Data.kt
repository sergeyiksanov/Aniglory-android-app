package com.example.aniglory_app.fragments.data

import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.models.kodik.TitlesModelKodik
import com.example.aniglory_app.models.kodik.epModel
import com.example.aniglory_app.models.kodik.resultsModel
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
import kotlin.properties.Delegates

object Data {

    var watching_name: String? = null
    var watching_poster: String? = null
    var watching_episode: String? = null


    lateinit var current_movie: String

    lateinit var editUserResourse : String

    lateinit var content_category: String

    lateinit var category: String

    var description: String = "Описание отсутствует"

    var type_login by Delegates.notNull<Boolean>()
    lateinit var email: String
    lateinit var username: String

    lateinit var next_page: String
    var list_duplicate: MutableList<resultsModel> = mutableListOf()

    var code_title: String = ""

    var player_type: String = "kodik"

//    var count_episodes by Delegates.notNull<Int>()
    lateinit var episodesDataKodik: HashMap<String, String>
    lateinit var episodesDataAnilibria: HashMap<String, epModel>
    lateinit var hostAnilibria: String

    var isLoading = false
    var num_request = 0
    var type_request = "base"
    var adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
//        startEpisodes(id)
    }

    fun fun_is_development(context: Context) {
        val title = "Эта функция находится в разработке"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, title, duration)
        toast.show()
    }

    fun baseTitles(type: String, parameters: Map<String, String>, URL: String = next_page) {
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(URL)
                    parameter("token", Network.API_TOKEN)
//                    parameter("limit", "9")
//                    parameter("filter", "id,code,names,posters")
//                    if(num_request != 0 && type_request == "base"){
//                        parameter("after", (num_request * 9).toString())
//                    }

                    parameter("lgbt", false)
                    parameter("minimal_age", "0-18")
                    for((key, value) in parameters) {
                        parameter(key, value)
                    }

                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    val titles: List<resultsModel> = deleteDuplicate(Gson().fromJson(data, TitlesModelKodik::class.java).results)
                    withContext(Dispatchers.Main) {

                        Log.i("NETWORK_TITLE", titles.toString())

                        list_duplicate.addAll(titles)

                        if(type_request == "base") {
                            list_duplicate = deleteDuplicate(list_duplicate) as MutableList<resultsModel>
                            next_page = data.asJsonObject["next_page"].asString
                        }
//                        titles.distinctBy { it.title_orig in list_duplicate }
//                        for(it in titles) {
//                            list_duplicate.add(it.title_orig)
//                        }

//                        if(type == "update"){
//                            adapter.updateAdapter(list_duplicate)
////                        }
//                        else if(type == "create"){
                            adapter.createNewAdapter(list_duplicate)
//                        }
                    }
                }
            }
            catch (err: Exception){
                withContext(Dispatchers.Main){
                    Log.i("NETWORK_TITLE", err.toString())
                    Log.i("NETWORK_TITLE", "URL: $URL, PARAMETERS: $parameters")
                }
            }
        }
        isLoading = false
    }

    fun deleteDuplicate(titles: MutableList<resultsModel>): MutableList<resultsModel> = titles.distinctBy { it.title_orig } as MutableList<resultsModel>
}