package com.example.aniglory_app.fragments.footer.new_interface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentNavigationMenuBinding
import com.example.aniglory_app.fragments.body.BookmarksFragment
import com.example.aniglory_app.fragments.body.ProfileFragment
import com.example.aniglory_app.fragments.body.SearchFragment
import com.example.aniglory_app.fragments.body.TitlesFragment
import com.example.aniglory_app.fragments.body.new_interface.NewProfileFragment
import com.example.aniglory_app.fragments.footer.MenuFragment

class NavigationMenuFragment : Fragment() {
    lateinit var binding: FragmentNavigationMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentNavigationMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.iSearch.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.final_search_primary)
            binding.iBookmark.setImageResource(R.drawable.final_bookmarks)
            binding.iProfile.setImageResource(R.drawable.final_profile)
            binding.iHome.setImageResource(R.drawable.final_home)

            startFragment(SearchFragment.newInstance())
        }
        binding.iBookmark.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.final_search)
            binding.iBookmark.setImageResource(R.drawable.final_bookmarks_primary)
            binding.iProfile.setImageResource(R.drawable.final_profile)
            binding.iHome.setImageResource(R.drawable.final_home)

            startFragment(BookmarksFragment())
        }
        binding.iProfile.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.final_search)
            binding.iBookmark.setImageResource(R.drawable.final_bookmarks)
            binding.iProfile.setImageResource(R.drawable.final_profile_primary)
            binding.iHome.setImageResource(R.drawable.final_home)

            startFragment(NewProfileFragment())
        }
        binding.iHome.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.final_search)
            binding.iBookmark.setImageResource(R.drawable.final_bookmarks)
            binding.iProfile.setImageResource(R.drawable.final_profile)
            binding.iHome.setImageResource(R.drawable.final_home_primary)

            startFragment(TitlesFragment.newInstance())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NavigationMenuFragment().apply {
                arguments = Bundle()
            }
    }

    private fun startFragment(newInstance: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.bodyFragment, newInstance)?.commit()
    }
}