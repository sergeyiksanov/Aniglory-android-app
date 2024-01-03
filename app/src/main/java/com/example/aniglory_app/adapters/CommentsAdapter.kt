package com.example.aniglory_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.CommentBinding
import com.example.aniglory_app.databinding.TitleBinding
import com.example.aniglory_app.databinding.TitleHorizontalBinding
import com.example.aniglory_app.values.Network
import com.example.aniglory_app.models.anilibria.TitlesModel
import com.example.aniglory_app.models.anilibria.listModel
import com.example.aniglory_app.models.firebase.Comment
import com.example.aniglory_app.models.kodik.resultsModel

class CommentsAdapter(var data: MutableList<Comment> = mutableListOf(), val callbacks: (view: View) -> Unit): RecyclerView.Adapter<CommentsAdapter.CommentsHolder>() {

    inner class CommentsHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CommentBinding.bind(item)

        fun bind(title: Comment) = with(binding) {
            avatar.load(title.avatar)
            username.text = title.username
            comment.text = title.comment

            Log.i("DATA_USER", "${title.avatar} ${title.username} ${title.comment}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment, parent, false)

        return CommentsHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.bind(data[position])
    }

    fun updateAdapter(newData: Comment){
        data.addAll(listOf(newData))
        notifyDataSetChanged()
    }

    fun createNewAdapter(newData: Comment){
        data = mutableListOf(newData)
        notifyDataSetChanged()
    }
}