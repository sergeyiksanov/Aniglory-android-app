package com.example.aniglory_app.fragments.body

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.CategoryAdapter
import com.example.aniglory_app.adapters.EpisodesAdapter
import com.example.aniglory_app.databinding.FragmentCategoriesBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.kodik.GenresModel
import com.example.aniglory_app.models.kodik.TitlesModelKodik
import com.example.aniglory_app.models.kodik.YearsModel
import com.example.aniglory_app.models.kodik.result
import com.example.aniglory_app.models.kodik.resultsModel
import com.example.aniglory_app.models.kodik.years
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

class CategoriesFragment : Fragment() {

    lateinit var binding: FragmentCategoriesBinding
    lateinit var layoutManager: LinearLayoutManager
    private val adapter = CategoryAdapter() {
        val name = it.findViewById<TextView>(R.id.name).text.toString()
        Log.i("NETWORK_TITLE", name)
        startFragment(name)
    }

    private fun startFragment(content_category: String) {
        Data.content_category = content_category

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, ContentFragment.newInstance())?.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when(Data.category) {
            "genres" -> {
                binding.categoryName.text = "ЖАНРЫ:"
            }
            "countries" -> {
                binding.categoryName.text = "СТРАНЫ:"
            }
            "years" -> {
                binding.categoryName.text = "ГОДА:"
            }
        }

        layoutManager = LinearLayoutManager(activity)
        init()
        getData()
    }
//ПИМЬКА
    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    when(Data.category) {
                        "genres" -> {
                            url(Network.GENRES_KODIK)
                            parameter("genres_type", "shikimori")
                            parameter("lgbt", false)
                            parameter("minimal_age", "0-18")
                        }
                        "countries" -> {
                            url(Network.KODIK_COUNTRIES)
                            parameter("types", "anime,anime-serial")
                        }
                        "years" -> {
                            url(Network.KODIK_YEARS)
                            parameter("types", "anime,anime-serial")
                        }
                    }
                    parameter("token", Network.API_TOKEN)

                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()

                    withContext(Dispatchers.Main) {
                        val names_list = mutableListOf<String>()

                        when(Data.category) {
                            "genres" -> {
                                val names: List<result> = Gson().fromJson(data, GenresModel::class.java).results

                                for(it in names) {
                                    names_list.add(it.title.uppercase())
                                }
                            }
                            "countries" -> {
                                val names: List<result> = Gson().fromJson(data, GenresModel::class.java).results

                                for(it in names) {
                                    names_list.add(it.title.uppercase())
                                }
                            }
                            "years" -> {
                                val names: List<years> = Gson().fromJson(data, YearsModel::class.java).results

                                for(it in names) {
                                    names_list.add(it.year.uppercase())
                                }
                            }
                        }

                        Log.i("NETWORK_TITLE", names_list.toString())
                        adapter.createNewAdapter(names_list)
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

    companion object {
        @JvmStatic
        fun newInstance() =
            CategoriesFragment().apply {
                arguments = Bundle()
            }
    }
}