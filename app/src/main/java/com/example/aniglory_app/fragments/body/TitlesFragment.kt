package com.example.aniglory_app.fragments.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.FragmentTitlesBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.values.Network

class TitlesFragment : Fragment() {

    lateinit var binding: FragmentTitlesBinding
    lateinit var layoutManager: LinearLayoutManager
    private val adapter = TitlesAdapter() {
        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
        startTitle(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTitlesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.VISIBLE

        Data.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        Data.list_duplicate.clear()
        init()

        var parameters = mutableMapOf(
//            "limit" to "9",
            "filter" to "id,code,names,posters,with_material_data",
            "limit" to "9",
            "types" to "anime,anime-serial",
            "with_material_data" to "true"
        )

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
                            Data.baseTitles("update", mapOf(
                                "limit" to "9",
                                "types" to "anime,anime-serial",
                                "with_material_data" to "true"
                            ))
                        }
                    }
                }
            }
        })
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    companion object {
        fun newInstance() =
            TitlesFragment().apply {
                arguments = Bundle()
            }
    }

    fun startTitle(code: String) {
        Data.code_title = code

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, TitleFragment.newInstance())?.commit()
    }
}