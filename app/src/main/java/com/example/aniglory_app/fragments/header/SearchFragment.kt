package com.example.aniglory_app.fragments.header

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.FragmentSearchBinding
import com.example.aniglory_app.fragments.body.TitleFragment
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.kodik.TitlesModelKodik
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

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var layoutManager: LinearLayoutManager

    private val adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
        startTitle(id)
    }

    fun startTitle(code: String) {
        Data.code_title = code

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, TitleFragment.newInstance())?.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        layoutManager = LinearLayoutManager(activity)
        init()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
//                if(binding.searchView.query.isEmpty()){
//                    Data.list_duplicate = mutableListOf()
//                    Data.baseTitles("create", mapOf(
//                        "limit" to "9",
//                        "types" to "anime,anime-serial",
//                        "with_material_data" to "true"
//                    ), Network.KODIK_LIST)
//                    Data.type_request = "base"
//                }
//                else{

                    baseTitles(mapOf(
                        "title" to p0.toString(),
                        "types" to "anime,anime-serial",
                        "with_material_data" to "true"
                    ), Network.KODIK_PLAYER)

//                }

                adapter.createNewAdapter(mutableListOf())

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
//                if(binding.searchView.query.isEmpty()){
//                    Data.list_duplicate = mutableListOf()
//                    Data.baseTitles("create", mapOf(
//                        "limit" to "9",
//                        "types" to "anime,anime-serial",
//                        "with_material_data" to "true"
//                    ), Network.KODIK_LIST)
//                    Data.type_request = "base"
//                }
//                else{

                    baseTitles(mapOf(
                        "title" to p0.toString(),
                        "types" to "anime,anime-serial",
                        "with_material_data" to "true"
                    ), Network.KODIK_PLAYER)
//                }

                adapter.createNewAdapter(mutableListOf())

                return true
            }

        })
    }

    private fun baseTitles(parameters: Map<String, String>, URL: String) {
        Data.isLoading = true

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

                    for((key, value) in parameters) {
                        parameter(key, value)
                    }

                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    val titles: List<resultsModel> = Data.deleteDuplicate(
                        Gson().fromJson(
                            data,
                            TitlesModelKodik::class.java
                        ).results
                    )
                    withContext(Dispatchers.Main) {

                        Log.i("NETWORK_TITLE", titles.toString())

//                        titles.distinctBy { it.title_orig in list_duplicate }
//                        for(it in titles) {
//                            list_duplicate.add(it.title_orig)
//                        }

//                        if(type == "update"){
//                            adapter.updateAdapter(list_duplicate)
////                        }
//                        else if(type == "create"){
                        adapter.createNewAdapter(titles as MutableList<resultsModel>)
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
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle()
            }
    }
}