package com.example.aniglory_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.CategoryBinding
import com.example.aniglory_app.databinding.EpisodeBinding

class CategoryAdapter(
    var data: MutableList<String> = mutableListOf(),
    val callbacks: (view: View) -> Unit
): RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    inner class CategoryHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CategoryBinding.bind(item)

        fun bind(pos: Int, n: String) = with(binding) {
            position.text = pos.toString()
            name.text = n
            root.setOnClickListener {
                callbacks(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category, parent, false)

        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryHolder, position: Int) {
        holder.bind(position, data[position])
    }

    override fun getItemCount(): Int = data.size

    fun createNewAdapter(newData: MutableList<String>){
        data = newData
        notifyDataSetChanged()
    }
}