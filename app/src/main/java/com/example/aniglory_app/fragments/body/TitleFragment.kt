package com.example.aniglory_app.fragments.body

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.activites.players.PlayerAnilibriaActivity
//import com.example.aniglory_app.activites.other.EpisodesActivity
import com.example.aniglory_app.databinding.FragmentTitleBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.kodik.TitlesModelKodik
import com.example.aniglory_app.models.kodik.resultsModel
import com.example.aniglory_app.values.Network
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

class TitleFragment : Fragment() {

    lateinit var dialog: Dialog
    lateinit var title: resultsModel
    lateinit var binding: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTitleBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.code.text = Data.code_title
        dialog = Dialog(requireActivity())

        binding.share.setOnClickListener { Data.fun_is_development(requireActivity()) }
        binding.stars.setOnClickListener { Data.fun_is_development(requireActivity()) }
        binding.addBookmarks.setOnClickListener { Data.fun_is_development(requireActivity()) }
        binding.comments.setOnClickListener { startFragment(CommentsFragment.newInstance()) }

        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.GONE

        binding.readFull.setOnClickListener { startDescription() }

        BottomSheetBehavior.from(binding.sheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        getData(Data.code_title)
        searchAnilibriaPlayer()
        binding.bWatch.setOnClickListener { bWatch() }
        binding.closeFull.setOnClickListener { bCloseFragment() }
    }

    private fun startFragment(newInstance: Fragment) {
        Data.current_movie = title.id.toString()

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, newInstance)?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TitleFragment().apply {
                arguments = Bundle()
            }
    }

    private fun startDescription() {
        Data.description = title.material_data?.description.toString()

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, DescriptionFragment.newInstance())
            ?.commit()
    }

    private fun getData(code: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.KODIK_PLAYER)
                    parameter("token", Network.API_TOKEN)
                    parameter("id", code)
                    Log.i("NETWORK_TITLE", code)
                    parameter("type", "anime,anime-serial")
                    parameter("with_material_data", "true")
                    parameter("with_episodes", "true")
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()
                    title = Gson().fromJson(data, TitlesModelKodik::class.java).results[0]
                    withContext(Dispatchers.Main) {
                        Log.i("NETWORK_TITLE", title.seasons.toString())

                        if(title.seasons != null) {
                            for(it in title.seasons!!) {
                                Data.episodesDataKodik = it.value.episodes!!
                            }
                        }


                        Log.i("NETWORK_TITLE", title.toString())
                        binding.backgroundPoster.load(title.material_data?.poster_url)
                        binding.poster.load(title.material_data?.poster_url)

                        binding.title.text = title.title
                        binding.description.text = title.material_data?.description
                        binding.descriptionFull.text = title.material_data?.description
//                        binding.originalTitle.text = "Оригинальное название - " + title.names.en
//                        title_orig = title.names.en
                        binding.season.text =  title.year
//                        if(title.type.episodes != null) binding.countEpisodes.text = "Количество эпизодов - " + title.type.episodes.toString()
//                        else binding.countEpisodes.text = "Количество эпизодов - 1"
//                        binding.genres.text = "Жанры - " + title.genres.toString().replace("[", "").replace("]", "")
//                        binding.bWatch.setOnClickListener { bWatch() }
//                        binding.readFull.setOnClickListener { bRead() }
//                        binding.close.setOnClickListener { bClose() }

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

    private fun bCloseFragment() {
        activity?.onBackPressed()
    }

    private fun showCustomDialog() {
        dialog.setContentView(R.layout.player_selection)
        val kodik: Button = dialog.findViewById(R.id.kodik)
        val anilibria: Button = dialog.findViewById(R.id.anilibria)

        kodik.setOnClickListener { b_kodik() }
        anilibria.setOnClickListener { b_anilibria() }

        dialog.show()
    }

    private fun searchAnilibriaPlayer() {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = Network.httpClient.request{
                    url(Network.SEARCH_URL)
                    parameter("search", title.title_orig)
                    parameter("filter", "names,player")

                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }

                if(response.status == HttpStatusCode.OK){
                    val data: JsonElement = response.body()

                    Log.i("NETWORK_TITLE", data.toString())

                    withContext(Dispatchers.Main) {
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

    private fun b_kodik() {
        Data.player_type = "kodik"

        dialog.dismiss()
        Data.watching_name = title.title!!
        Data.watching_poster = title.material_data?.poster_url.toString()

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, EpisodesFragment.newInstance())
            ?.commit()
    }

    private fun b_anilibria() {
        Data.fun_is_development(requireActivity())

//        Data.player_type = "anilibria"
//

//        val i = Intent(activity, PlayerAnilibriaActivity::class.java)
//        i.putExtra(Constants.PLAYER, Constants.ANILIBRIA)
//        i.putExtra(Constants.EPISODES, title.player.episodes.last.toString())
//        i.putExtra(Constants.CODE, title.code)
//        startActivity(i)

//        fragmentManager
//            ?.beginTransaction()
//            ?.addToBackStack(null)
//            ?.replace(R.id.bodyFragment, EpisodesFragment.newInstance())
//            ?.commit()
    }
}