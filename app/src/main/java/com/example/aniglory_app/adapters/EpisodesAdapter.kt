package com.example.aniglory_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.EpisodeBinding

class EpisodesAdapter(
    var data: MutableList<Pair<Int, String>> = mutableListOf(),
    val callbacks: (view: View) -> Unit
): RecyclerView.Adapter<EpisodesAdapter.EpisodesHolder>() {
    inner class EpisodesHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = EpisodeBinding.bind(item)

        fun bind(episode: Pair<Int, String>) = with(binding) {
            epNumber.text = episode.first.toString()
            episodeUrl.text = episode.second
            root.setOnClickListener {
                callbacks(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.episode, parent, false)

        return EpisodesHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: EpisodesHolder, position: Int) {
        holder.bind(data[position])
    }

    fun createNewAdapter(newData: MutableList<Pair<Int, String>>){
        data = newData
        notifyDataSetChanged()
    }
}