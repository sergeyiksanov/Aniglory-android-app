package com.example.aniglory_app.fragments.body

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aniglory_app.R
import com.example.aniglory_app.adapters.CommentsAdapter
import com.example.aniglory_app.adapters.TitlesAdapter
import com.example.aniglory_app.databinding.FragmentCommentsBinding
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.models.firebase.Comment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentsFragment : Fragment() {
    lateinit var binding: FragmentCommentsBinding
    lateinit var layoutManager: LinearLayoutManager
    private val adapter = CommentsAdapter() {
//        val id = it.findViewById<TextView>(R.id.titleID).text.toString()
//        startTitle(id)
    }

    lateinit var new_comment: Comment

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentsBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun init() = with(binding) {
        rcView.layoutManager = layoutManager
        rcView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(activity)
        init()
        adapter.updateAdapter(getData())
    }

    private fun getData(): Comment {
        new_comment = Comment("", "", "")

        Log.i("NETWORK_TITLE", Data.current_movie)

        val docRef = db.collection("users")

        docRef.get()
            .addOnCompleteListener { document ->
                if(document != null) {
                    if(document.result != null) {
                        Log.i("NETWORK_TITLE", document.result.toString())
                    }
                }
            }

        return new_comment
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CommentsFragment().apply {
                arguments = Bundle()
            }
    }
}