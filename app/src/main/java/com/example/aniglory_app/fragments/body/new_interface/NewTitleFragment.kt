package com.example.aniglory_app.fragments.body.new_interface

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentNewTitleBinding
import com.example.aniglory_app.databinding.FragmentTitleBinding
import com.example.aniglory_app.fragments.body.EpisodesFragment
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.kodik.TitlesModelKodik
import com.example.aniglory_app.models.kodik.resultsModel
import com.example.aniglory_app.values.Network
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
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

class NewTitleFragment : Fragment() {

    lateinit var binding: FragmentNewTitleBinding
    lateinit var title: resultsModel
    val db = Firebase.firestore
    lateinit var auth: FirebaseAuth
    private var typeBookmarks = "trash"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTitleBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun checkAuthState(): Boolean {
        return auth.currentUser != null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.GONE

        auth = Firebase.auth

        getData(Data.code_title)

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
                        binding.posterImg.load(title.material_data?.poster_url)

                        binding.titleTXt.text = title.title
                        binding.description.text = title.material_data?.description ?: "Отсутсвует"
                        binding.year.text =  "Год: ${title.year}"
                        binding.titleOtherTXt.text = title.title_orig
                        binding.cntEpisodes.text = "Серий: ${title.episodes_count}"
                        binding.episode.text = "~ ${title.material_data?.duration ?: "24"} мин"
                        binding.status.text = title.material_data?.anime_status ?: "Статус не известен"
                        binding.type.text = title.material_data?.anime_kind ?: "Тип не известен"
                        binding.studio.text = "Студия: ${title.material_data?.anime_studios?.get(0) ?: "Не известна"}"
                        binding.bookmarks.setOnClickListener { addToBookmarks(Data.code_title) }
                        binding.watch.setOnClickListener { bWatch() }
                        binding.mainGenre.text = title.material_data?.anime_genres?.get(0) ?: "Отсутствует"

                        binding.screen1.load(title.screenshots?.get(0))
                        binding.screen2.load(title.screenshots?.get(1))
                        binding.screen3.load(title.screenshots?.get(2))
                        binding.screen4.load(title.screenshots?.get(3))
                        binding.screen5.load(title.screenshots?.get(4))

                        if(title.material_data?.actors == null) {
                            binding.actors.text = "Не известны"
                        } else {
                            binding.actors.text = title.material_data?.actors.toString().replace("[", "").replace("]", "")
                        }
                        if(title.material_data?.directors == null) {
                            binding.directors.text = "Не известны"
                        } else {
                            binding.directors.text = title.material_data?.directors.toString().replace("[", "").replace("]", "")
                        }
                        if(title.material_data?.producers == null) {
                            binding.producers.text = "Не известны"
                        } else {
                            binding.producers.text = title.material_data?.producers.toString().replace("[", "").replace("]", "")
                        }
                        if(title.material_data?.writers == null) {
                            binding.writs.text = "Не известны"
                        } else {
                            binding.writs.text = title.material_data?.writers.toString().replace("[", "").replace("]", "")
                        }
                        if(title.material_data?.composers == null) {
                            binding.compositors.text = "Не известны"
                        } else {
                            binding.compositors.text = title.material_data?.composers.toString().replace("[", "").replace("]", "")
                        }

                        delBtn(code)

                        binding.posterImg.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.poster_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            val poster = dialog.findViewById<ImageView>(R.id.poster)
                            poster.load(title.material_data?.poster_url)
                            dialog.show()
                        }
                        binding.screen1.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.screenshot_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            val poster = dialog.findViewById<ImageView>(R.id.screenshot)
                            poster.load(title.screenshots?.get(0))
                            dialog.show()
                        }
                        binding.screen2.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.screenshot_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            val poster = dialog.findViewById<ImageView>(R.id.screenshot)
                            poster.load(title.screenshots?.get(1))
                            dialog.show()
                        }
                        binding.screen3.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.screenshot_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            val poster = dialog.findViewById<ImageView>(R.id.screenshot)
                            poster.load(title.screenshots?.get(2))
                            dialog.show()
                        }
                        binding.screen4.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.screenshot_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            val poster = dialog.findViewById<ImageView>(R.id.screenshot)
                            poster.load(title.screenshots?.get(3))
                            dialog.show()
                        }
                        binding.screen5.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.screenshot_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            val poster = dialog.findViewById<ImageView>(R.id.screenshot)
                            poster.load(title.screenshots?.get(4))
                            dialog.show()
                        }

                        binding.genres.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.more_info_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val titleTxt = dialog.findViewById<TextView>(R.id.titleTXt)
                            val titleOther = dialog.findViewById<TextView>(R.id.titleOtherTXt)
                            val cntEpisodes = dialog.findViewById<TextView>(R.id.cntEpisodes)
                            val episode = dialog.findViewById<TextView>(R.id.episode)
                            val year = dialog.findViewById<TextView>(R.id.year)
                            val status = dialog.findViewById<TextView>(R.id.status)
                            val type = dialog.findViewById<TextView>(R.id.type)
                            val studio = dialog.findViewById<TextView>(R.id.studio)
                            val genres = dialog.findViewById<TextView>(R.id.genres)

                            titleTxt.text = title.title
                            year.text =  "Год: ${title.year}"
                            titleOther.text = title.title_orig
                            cntEpisodes.text = "Серий: ${title.episodes_count}"
                            episode.text = "~ ${title.material_data?.duration ?: "24"} мин"
                            status.text = title.material_data?.anime_status ?: "Статус не известен"
                            type.text = title.material_data?.anime_kind ?: "Тип не известен"
                            if(title.material_data?.anime_studios != null) {
                                studio.text = "Студии: ${title.material_data?.anime_studios.toString().replace("[", "").replace("]", "")}"
                            } else {
                                studio.text = "Студии: Отсутствуют"
                            }
                            if(title.material_data?.anime_genres != null) {
                                genres.text = "Жанры: ${title.material_data?.anime_genres.toString().replace("[", "").replace("]", "")}"
                            } else {
                                studio.text = "Жанры: Отсутствуют"
                            }

                            dialog.show()
                        }

                        binding.info.setOnClickListener {
                            val dialog = Dialog(requireActivity())
                            dialog.setContentView(R.layout.more_info_dialog)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val titleTxt = dialog.findViewById<TextView>(R.id.titleTXt)
                            val titleOther = dialog.findViewById<TextView>(R.id.titleOtherTXt)
                            val cntEpisodes = dialog.findViewById<TextView>(R.id.cntEpisodes)
                            val episode = dialog.findViewById<TextView>(R.id.episode)
                            val year = dialog.findViewById<TextView>(R.id.year)
                            val status = dialog.findViewById<TextView>(R.id.status)
                            val type = dialog.findViewById<TextView>(R.id.type)
                            val studio = dialog.findViewById<TextView>(R.id.studio)
                            val genres = dialog.findViewById<TextView>(R.id.genres)

                            titleTxt.text = title.title
                            year.text =  "Год: ${title.year}"
                            titleOther.text = title.title_orig
                            cntEpisodes.text = "Серий: ${title.episodes_count}"
                            episode.text = "~ ${title.material_data?.duration ?: "24"} мин"
                            status.text = title.material_data?.anime_status ?: "Статус не известен"
                            type.text = title.material_data?.anime_kind ?: "Тип не известен"
                            if(title.material_data?.anime_studios != null) {
                                studio.text = "Студии: ${title.material_data?.anime_studios.toString().replace("[", "").replace("]", "")}"
                            } else {
                                studio.text = "Студии: Отсутствуют"
                            }
                            if(title.material_data?.anime_genres != null) {
                                genres.text = "Жанры: ${title.material_data?.anime_genres.toString().replace("[", "").replace("]", "")}"
                            } else {
                                studio.text = "Жанры: Отсутствуют"
                            }

                            dialog.show()
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
    }

    private fun delBtn(code: String) {
        if(checkAuthState()) {
            val docRef = db.collection("users").document(auth.currentUser!!.uid)

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val watchingList = document.get("watching") as? List<String>
                    val watchedList = document.get("watched") as? List<String>
                    val planeList = document.get("plane") as? List<String>
                    val postponedList = document.get("postponed") as? List<String>
                    val abandonedList = document.get("abandoned") as? List<String>

                    watchingList?.let { existingCodes ->
                        if (existingCodes.contains(code)) {
                            binding.savedBookmarks.text = "Смотрю"
                            binding.savedBookmarks.visibility = View.VISIBLE
                            binding.bookmarks.setOnClickListener {
                                docRef.update("watching", FieldValue.arrayRemove(code))
                                binding.savedBookmarks.text = ""
                                binding.savedBookmarks.visibility = View.GONE
                                binding.bookmarks.setOnClickListener { addToBookmarks(Data.code_title) }
                            }
                        }
                    }

                    watchedList?.let { existingCodes ->
                        if (existingCodes.contains(code)) {
                            binding.savedBookmarks.text = "Просмотренно"
                            binding.savedBookmarks.visibility = View.VISIBLE
                            binding.bookmarks.setOnClickListener {
                                docRef.update("watched", FieldValue.arrayRemove(code))
                                binding.savedBookmarks.text = ""
                                binding.savedBookmarks.visibility = View.GONE
                                binding.bookmarks.setOnClickListener { addToBookmarks(Data.code_title) }
                            }
                        }
                    }

                    planeList?.let { existingCodes ->
                        if (existingCodes.contains(code)) {
                            binding.savedBookmarks.text = "В планах"
                            binding.savedBookmarks.visibility = View.VISIBLE
                            binding.bookmarks.setOnClickListener {
                                docRef.update("plane", FieldValue.arrayRemove(code))
                                binding.savedBookmarks.text = ""
                                binding.savedBookmarks.visibility = View.GONE
                                binding.bookmarks.setOnClickListener { addToBookmarks(Data.code_title) }
                            }
                        }
                    }

                    postponedList?.let { existingCodes ->
                        if (existingCodes.contains(code)) {
                            binding.savedBookmarks.text = "Отложено"
                            binding.savedBookmarks.visibility = View.VISIBLE
                            binding.bookmarks.setOnClickListener {
                                docRef.update("postponed", FieldValue.arrayRemove(code))
                                binding.savedBookmarks.text = ""
                                binding.savedBookmarks.visibility = View.GONE
                                binding.bookmarks.setOnClickListener { addToBookmarks(Data.code_title) }
                            }
                        }
                    }

                    abandonedList?.let { existingCodes ->
                        if (existingCodes.contains(code)) {
                            binding.savedBookmarks.text = "Брошено"
                            binding.savedBookmarks.visibility = View.VISIBLE
                            binding.bookmarks.setOnClickListener {
                                docRef.update("abandoned", FieldValue.arrayRemove(code))
                                binding.savedBookmarks.text = ""
                                binding.savedBookmarks.visibility = View.GONE
                                binding.bookmarks.setOnClickListener { addToBookmarks(Data.code_title) }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewTitleFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun addToBookmarks(id: String) {
        if(checkAuthState()) {
            val docRef = db.collection("users").document(auth.currentUser!!.uid)

            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.bookmarks_selection)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val watching = dialog.findViewById<Button>(R.id.watching)
            val watched = dialog.findViewById<Button>(R.id.watched)
            val plane = dialog.findViewById<Button>(R.id.plane)
            val postponed = dialog.findViewById<Button>(R.id.postponed)
            val abandoned = dialog.findViewById<Button>(R.id.abandoned)

            watching.setOnClickListener {
                docRef.get().addOnSuccessListener {
                    if(it.exists()) {
                        docRef.update("watching", FieldValue.arrayUnion(id))
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    } else {
                        val userData = hashMapOf<String, Any>("watching" to arrayListOf(id))
                        docRef.set(userData)
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    }
                }

                delBtn(id)

                dialog.dismiss()
            }
            watched.setOnClickListener {
                docRef.get().addOnSuccessListener {
                    if(it.exists()) {
                        docRef.update("watched", FieldValue.arrayUnion(id))
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    } else {
                        val userData = hashMapOf<String, Any>("watched" to arrayListOf(id))
                        docRef.set(userData)
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    }
                }
                delBtn(id)
                dialog.dismiss()
            }
            plane.setOnClickListener {
                docRef.get().addOnSuccessListener {
                    if(it.exists()) {
                        docRef.update("plane", FieldValue.arrayUnion(id))
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    } else {
                        val userData = hashMapOf<String, Any>("plane" to arrayListOf(id))
                        docRef.set(userData)
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    }
                }

                delBtn(id)
                dialog.dismiss()
            }
            postponed.setOnClickListener {
                docRef.get().addOnSuccessListener {
                    if(it.exists()) {
                        docRef.update("postponed", FieldValue.arrayUnion(id))
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    } else {
                        val userData = hashMapOf<String, Any>("postponed" to arrayListOf(id))
                        docRef.set(userData)
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    }
                }

                delBtn(id)
                dialog.dismiss()
            }
            abandoned.setOnClickListener {
                docRef.get().addOnSuccessListener {
                    if(it.exists()) {
                        docRef.update("abandoned", FieldValue.arrayUnion(id))
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    } else {
                        val userData = hashMapOf<String, Any>("abandoned" to arrayListOf(id))
                        docRef.set(userData)
                            .addOnSuccessListener {
                                Log.i("ADD BOOK", "success")
                            }
                            .addOnFailureListener { exception ->
                                Log.i("ADD BOOK", exception.toString())
                            }
                    }
                }

                delBtn(id)
                dialog.dismiss()
            }

            dialog.show()
        } else {
            val title = "Войдите в аккаунт"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(activity, title, duration)
            toast.show()
        }
    }

    private fun bWatch() {
        Data.watching_name = title.title!!
        Data.watching_poster = title.material_data?.poster_url.toString()
        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, EpisodesFragment.newInstance())
            ?.commit()
    }

}