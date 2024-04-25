package com.example.aniglory_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.TitleHorizontalBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.anilibria.listModel
import com.example.aniglory_app.models.firebase.Comment
import com.example.aniglory_app.models.kodik.resultsModel

class TitlesAdapter(var data: MutableList<resultsModel> = mutableListOf(), val callbacks: (view: View) -> Unit): RecyclerView.Adapter<TitlesAdapter.TitlesHolder>() {

    inner class TitlesHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = TitleHorizontalBinding.bind(item)

        fun bind(title: resultsModel) = with(binding) {

            try {
//                if(title.material_data?.poster_url!!.substring(0, 17) == "https://shikimori.") {
//                    poster.load("https://shikimori.one" + title.material_data.poster_url.substring(18))
//                }
//                else {
                    poster.load(title.material_data?.poster_url)
//                }
                nameTitle.text = title.title.toString()
                titleID.text = title.id.toString()
                var y = title.year
                var e = title.episodes_count
                var d = title.material_data?.description

                if(title.year == null) {
                    y = "Неизветсно"
                }
                if(title.episodes_count == null) {
                    e = "Неизвестно"
                }
                if(title.material_data?.description == null) {
                    d = "Неизвестно"
                }
                otherData.text = "Год: ${y}, Эпизодов: ${e}".toString()
                description.text = d
                root.setOnClickListener {
                    callbacks(it)
                }
            } catch (e: Error) {
                Log.i("NETWORK_TITLE", e.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitlesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.title_horizontal, parent, false)

        return TitlesHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TitlesHolder, position: Int) {
        holder.bind(data[position])
    }

    fun updateAdapter(newData: resultsModel){
//        data.addAll(newData)


        if(data.isEmpty()) {
            data = mutableListOf(newData)
        }
        else {
            data.add(newData)
            data = Data.deleteDuplicate(data)
        }
        notifyDataSetChanged()
    }

    fun createNewAdapter(newData: MutableList<resultsModel>){
        if(newData == null) {
            Log.i("NETWORK_TITLE", "data is null")
        }
        else {
            data = newData
            notifyDataSetChanged()
        }
    }
}