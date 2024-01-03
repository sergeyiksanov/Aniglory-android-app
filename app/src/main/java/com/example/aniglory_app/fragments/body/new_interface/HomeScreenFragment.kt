package com.example.aniglory_app.fragments.body.new_interface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.activites.players.PlayerKodikActivity
import com.example.aniglory_app.databinding.FragmentHomeScreenBinding
import com.example.aniglory_app.fragments.body.TitlesFragment
import com.example.aniglory_app.models.kodik.resultsModel
import com.example.aniglory_app.values.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar

data class LastTitle (
    var name : String,
    var poster : String,
    var progress : Int,
    var url : String,
    var episode: String,
    var start_time: Int
)

class HomeScreenFragment : Fragment() {

    lateinit var binding: FragmentHomeScreenBinding

    lateinit var auth: FirebaseAuth

    lateinit var data_title : resultsModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun getSavedData(): LastTitle {
        val sharedPreferences = activity?.getSharedPreferences("last_watching_title", Context.MODE_PRIVATE)

        val name = sharedPreferences?.getString("name", "none")
        val poster = sharedPreferences?.getString("poster", "none")
        val progress = sharedPreferences?.getInt("progress", -1)
        Log.i("TEST_JS", progress.toString() + " is getSavedData()")
        val url = sharedPreferences?.getString("url", "none")
        val episode = sharedPreferences?.getString("episode", "-1") + " серия"
        val start_time = sharedPreferences?.getInt("start_time", 0)

        val res: LastTitle = LastTitle(name = name.toString(), poster = poster.toString(), progress = progress!!, url = url.toString(), episode = episode, start_time = start_time!!)
        return res
    }

    private fun startPlayerKodik(url: String, startTime: Int) {
        val i = Intent(activity, PlayerKodikActivity::class.java)
        i.putExtra(Constants.PLAYER_URL, (url))
        i.putExtra("start_time", startTime)
        startActivity(i)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.GONE

        auth = Firebase.auth
        setData()
    }

    private fun showDataLastTitle(title: LastTitle) {
        if(title.name != "none" && title.progress != -1 && title.url != "none") {
            binding.titleTxt.text = title.name
            binding.titleImg.load(title.poster)
            binding.progressBar.progress = title.progress!!
            Log.i("TEST_JS", title.progress.toString())
            binding.tProgress.text = title.progress.toString() + "%"
            binding.episode.text = title.episode
            binding.nextBtn.setOnClickListener {
                startPlayerKodik(title.url, title.start_time)
            }
        }
    }

//    private fun getData(code: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try{
//                val response = Network.httpClient.request{
//                    url(Network.KODIK_PLAYER)
//                    parameter("token", Network.API_TOKEN)
//                    parameter("id", code)
//                    Log.i("NETWORK_TITLE", code)
//                    parameter("type", "anime,anime-serial")
//                    parameter("with_material_data", "true")
//                    parameter("with_episodes", "true")
//                    method = HttpMethod.Get
//                    contentType(ContentType.Application.Json)
//                }
//
//                if(response.status == HttpStatusCode.OK){
//                    val data: JsonElement = response.body()
//                    data_title = Gson().fromJson(data, TitlesModelKodik::class.java).results[0]
//                    withContext(Dispatchers.Main) {
//                        Log.i("NETWORK_TITLE", data_title!!.seasons.toString())
//
//                        binding.titleImg.load(data_title.material_data?.poster_url)
//                        binding.titleTxt.text = data_title.title
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

    private fun getTime(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    private fun setData() {
        val time = getTime()
        var buffer: String
        if(time < 6) buffer = "Доброй ночи"
        else if(time < 12) buffer = "Доброе утро"
        else if(time < 18) buffer = "Добрый день"
        else buffer = "Добрый вечер"
        if(checkAuthState()) {
            binding.helloText.text = "$buffer, ${auth.currentUser!!.displayName}!"
        }
        else {
            binding.helloText.text = "$buffer, гость!"
        }
        Log.i("TEST_TIME", time.toString())
        showDataLastTitle(getSavedData())
        binding.btnSkip.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.bodyFragment, TitlesFragment.newInstance())
                ?.commit()
        }
    }

    private fun checkAuthState(): Boolean {
        return auth.currentUser != null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}