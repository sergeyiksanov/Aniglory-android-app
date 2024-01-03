package com.example.aniglory_app.fragments.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.FragmentContentBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.values.Network

class ContentFragment : Fragment() {

    lateinit var binding: FragmentContentBinding
    lateinit var layoutManager: LinearLayoutManager
    private val adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
        startTitle(id)
    }
    lateinit var parameters: MutableMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Data.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        Data.list_duplicate.clear()
        init()

        parameters = mutableMapOf(
//            "filter" to "id,code,names,posters"
            "with_material_data" to "true"
//            "all_genres" to Data.content_category
        )

        when(Data.category) {
            "genres" -> {
                parameters["types"] = "anime,anime-serial"
                parameters["all_genres"] = Data.content_category
            }
            "countries" -> {
                parameters["types"] = "anime,anime-serial"
                parameters["countries"] = Data.content_category
            }
            "years" -> {
                parameters["types"] = "anime,anime-serial"
                parameters["year"] = Data.content_category
            }
            "anime-serials" -> {
                parameters["types"] = "anime-serial"
            }
            "anime-movies" -> {
                parameters["types"] = "anime"
            }
        }

//        if(Data.num_request != 0) {
//            parameters["after"] = Data.num_request.toString()
//        }

        Data.baseTitles("create", parameters, Network.KODIK_LIST)

        binding.rcView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(Data.type_request == "base") {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if(!Data.isLoading) {
                        if( (visibleItemCount+firstVisibleItems) >= totalItemCount) {
                            Data.num_request += 1
                            Data.baseTitles("update", parameters)
                        }
                    }
                }
            }
        })
    }

    fun startTitle(code: String) {
        Data.code_title = code

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, TitleFragment.newInstance())?.commit()
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContentFragment().apply {
                arguments = Bundle()
            }
    }
}