package com.example.aniglory_app.fragments.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentDescriptionBinding
import com.example.aniglory_app.fragments.data.Data

class DescriptionFragment : Fragment() {

    lateinit var binding: FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDescriptionBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.GONE

        binding.description.text = Data.description
        binding.back.setOnClickListener { bCloseFragment() }
    }

    private fun bCloseFragment() {
        activity?.onBackPressed()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DescriptionFragment().apply {
                arguments = Bundle()
            }
    }
}