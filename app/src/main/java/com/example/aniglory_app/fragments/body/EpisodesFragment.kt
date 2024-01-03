package com.example.aniglory_app.fragments.body

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aniglory_app.activites.players.PlayerAnilibriaActivity
import com.example.aniglory_app.R
import com.example.aniglory_app.activites.players.PlayerKodikActivity
import com.example.aniglory_app.adapters.EpisodesAdapter
import com.example.aniglory_app.databinding.FragmentEpisodesBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.values.Constants
import com.example.aniglory_app.values.Network

class EpisodesFragment : Fragment() {

    lateinit var binding: FragmentEpisodesBinding
    lateinit var layoutManager: LinearLayoutManager
    private val adapter = EpisodesAdapter() {
        val num_episode = it.findViewById<TextView>(R.id.epNumber).text.toString()
        val url = it.findViewById<TextView>(R.id.episodeUrl).text.toString()
        Data.watching_episode = num_episode
        if(Data.player_type == "kodik") {
            startPlayerKodik(url)
        }
        else if(Data.player_type == "anilibria") {
            startPlayerAnilibria(url)
        }
    }

    private fun startPlayerKodik(url: String) {
        val i = Intent(activity, PlayerKodikActivity::class.java)
        i.putExtra(Constants.PLAYER_URL, (url))
        startActivity(i)
    }

    private fun startPlayerAnilibria(url: String) {
        val i = Intent(activity, PlayerAnilibriaActivity::class.java)
        i.putExtra(Constants.PLAYER_URL, (Network.PLAYER_ANILIBRIA_HOST + url).replace("\"", ""))
        startActivity(i)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val episodes = Data.count_episodes

        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.VISIBLE

        binding.back.setOnClickListener { back() }

        layoutManager = LinearLayoutManager(activity)
        init()

        if(Data.player_type == "kodik") {
            val list_ep = mutableListOf<Pair<Int, String>>()

            for(el in Data.episodesDataKodik) {
                val p = Pair(el.key.toInt(), el.value)
                list_ep.add(p)
            }
//
//            list_ep.sortedWith(compareBy{ it.first })
//            list_ep.sortedWith(compareBy{ it.first })

            list_ep.sortBy { it.first }

            Log.i("NETWORK_TITLE", list_ep.toString())

            adapter.createNewAdapter(list_ep)
        }
    }

    private fun back() {
        activity?.onBackPressed()
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EpisodesFragment().apply {
                arguments = Bundle()
            }
    }

    private fun getInt(it: String) : Int {
        Log.i("SORT", it.substringBefore(':'))
        return it.substringBefore(':').toInt()
    }
}