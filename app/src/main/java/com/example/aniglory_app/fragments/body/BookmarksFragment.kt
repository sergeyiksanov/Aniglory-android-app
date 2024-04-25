package com.example.aniglory_app.fragments.body

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aniglory_app.R
import com.example.aniglory_app.activites.other.TitleActivity
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.FragmentBookmarksBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.anilibria.listModel
import com.example.aniglory_app.models.kodik.TitlesModelKodik
import com.example.aniglory_app.models.kodik.resultsModel
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

class BookmarksFragment : Fragment() {

    lateinit var binding: FragmentBookmarksBinding

    var current_bookmark = "watching"
    val db = Firebase.firestore

    lateinit var auth: FirebaseAuth

    val background_primary = R.drawable.new_btn_full
    val background_default = R.drawable.btn_full

    private val adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
        startTitle(id)
    }
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarksBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun checkAuthState(): Boolean {
        return auth.currentUser != null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.VISIBLE

        auth = Firebase.auth

        layoutManager = LinearLayoutManager(activity)

        init()
        if(checkAuthState()) {
            getData()
        }
        else {
//            fragmentManager
//                ?.beginTransaction()
//                ?.addToBackStack(null)
//                ?.replace(R.id.bodyFragment, ProfileFragment.newInstance())?.commit()


        }

        binding.bWatching.setOnClickListener {
            binding.bWatching.setBackgroundResource(background_primary)
            binding.bWatched.setBackgroundResource(background_default)
            binding.bPlans.setBackgroundResource(background_default)
            binding.bPostponed.setBackgroundResource(background_default)
            binding.bAbandoned.setBackgroundResource(background_default)

            binding.bWatching.setTextColor(resources.getColor(R.color.btn_text))
            binding.bWatched.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPlans.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPostponed.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bAbandoned.setTextColor(resources.getColor(R.color.new_text_primary))

            var data: MutableList<resultsModel> = mutableListOf()
            adapter.createNewAdapter(data)
            current_bookmark = "watching"
            getData()
        }
        binding.bWatched.setOnClickListener {
            binding.bWatching.setBackgroundResource(background_default)
            binding.bWatched.setBackgroundResource(background_primary)
            binding.bPlans.setBackgroundResource(background_default)
            binding.bPostponed.setBackgroundResource(background_default)
            binding.bAbandoned.setBackgroundResource(background_default)

            binding.bWatching.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bWatched.setTextColor(resources.getColor(R.color.btn_text))
            binding.bPlans.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPostponed.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bAbandoned.setTextColor(resources.getColor(R.color.new_text_primary))

            var data: MutableList<resultsModel> = mutableListOf()
            adapter.createNewAdapter(data)
            current_bookmark = "watched"
            getData()
        }
        binding.bPlans.setOnClickListener {
            binding.bWatching.setBackgroundResource(background_default)
            binding.bWatched.setBackgroundResource(background_default)
            binding.bPlans.setBackgroundResource(background_primary)
            binding.bPostponed.setBackgroundResource(background_default)
            binding.bAbandoned.setBackgroundResource(background_default)

            binding.bWatching.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bWatched.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPlans.setTextColor(resources.getColor(R.color.btn_text))
            binding.bPostponed.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bAbandoned.setTextColor(resources.getColor(R.color.new_text_primary))

            var data: MutableList<resultsModel> = mutableListOf()
            adapter.createNewAdapter(data)
            current_bookmark = "plans"
            getData()
        }
        binding.bPostponed.setOnClickListener {
            binding.bWatching.setBackgroundResource(background_default)
            binding.bWatched.setBackgroundResource(background_default)
            binding.bPlans.setBackgroundResource(background_default)
            binding.bPostponed.setBackgroundResource(background_primary)
            binding.bAbandoned.setBackgroundResource(background_default)

            binding.bWatching.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bWatched.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPlans.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPostponed.setTextColor(resources.getColor(R.color.btn_text))
            binding.bAbandoned.setTextColor(resources.getColor(R.color.new_text_primary))

            var data: MutableList<resultsModel> = mutableListOf()
            adapter.createNewAdapter(data)
            current_bookmark = "postponed"
            getData()
        }
        binding.bAbandoned.setOnClickListener {
            binding.bWatching.setBackgroundResource(background_default)
            binding.bWatched.setBackgroundResource(background_default)
            binding.bPlans.setBackgroundResource(background_default)
            binding.bPostponed.setBackgroundResource(background_default)
            binding.bAbandoned.setBackgroundResource(background_primary)

            binding.bWatching.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bWatched.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPlans.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bPostponed.setTextColor(resources.getColor(R.color.new_text_primary))
            binding.bAbandoned.setTextColor(resources.getColor(R.color.btn_text))

            var data: MutableList<resultsModel> = mutableListOf()
            adapter.createNewAdapter(data)
            current_bookmark = "abandoned"
            getData()
        }
    }

    private fun baseTitles(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.KODIK_PLAYER)
                    parameter("token", Network.API_TOKEN)
                    parameter("id", id)
                    parameter("with_material_data", "true")
//                    parameter("filter", "id,code,names,posters")
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    val titles = Gson().fromJson(data, TitlesModelKodik::class.java).results
                    withContext(Dispatchers.Main) {
                        Log.i("NETWORK_TITLE", titles.toString())

                        adapter.updateAdapter(titles[0])
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

    private fun startTitle(code: String) {
        Data.code_title = code

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, TitleFragment.newInstance())?.commit()
    }

    private fun getData() {
        val docRef = db.collection("users").document(auth.currentUser!!.uid)

        docRef.get()
            .addOnCompleteListener { document ->
                if(document != null && document.result != null && document.result.data != null && document.result.data!![current_bookmark] != null) {
                    val id_list = document.result.data!![current_bookmark] as List<String>
                    for(it1 in id_list) {
                        baseTitles(it1)
                        Log.i("NETWORK_TITLE", it1)
                    }
                }
                Log.i("NETWORK_TITLE", document.result.toString())
            }
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookmarksFragment().apply {
                arguments = Bundle()
            }
    }
}