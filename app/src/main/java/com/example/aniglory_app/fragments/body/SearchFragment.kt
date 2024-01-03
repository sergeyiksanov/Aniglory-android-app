package com.example.aniglory_app.fragments.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentSearch2Binding
import com.example.aniglory_app.fragments.data.Data

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearch2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearch2Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.VISIBLE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.VISIBLE

        binding.genres.setOnClickListener {
            Data.category = "genres"
            startFragment(CategoriesFragment.newInstance())
        }
        binding.countries.setOnClickListener {
            Data.category = "countries"
            startFragment(CategoriesFragment.newInstance())
        }
        binding.years.setOnClickListener {
            Data.category = "years"
            startFragment(CategoriesFragment.newInstance())
        }
        binding.animeSerials.setOnClickListener {
            Data.category = "anime-serials"
            startFragment(ContentFragment.newInstance())
        }
        binding.animeMovies.setOnClickListener {
            Data.category = "anime-movies"
            startFragment(ContentFragment.newInstance())
        }

    }

    private fun startFragment(newInstance: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.bodyFragment, newInstance)
            ?.addToBackStack(null)
            ?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle()
            }
    }
}