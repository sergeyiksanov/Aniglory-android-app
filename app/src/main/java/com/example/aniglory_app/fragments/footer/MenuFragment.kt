package com.example.aniglory_app.fragments.footer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentMenuBinding
import com.example.aniglory_app.fragments.body.ProfileFragment
import com.example.aniglory_app.fragments.body.SettingsFragment
import com.example.aniglory_app.fragments.body.TitlesFragment
import com.example.aniglory_app.fragments.body.BookmarksFragment
import com.example.aniglory_app.fragments.body.SearchFragment

class MenuFragment : Fragment() {
    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bSearch.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.search_new_accent)
            binding.iBookmark.setImageResource(R.drawable.bookmarks_new)
            binding.iProfile.setImageResource(R.drawable.profile_new)
            binding.iHome.setImageResource(R.drawable.home_new)

            startFragment(SearchFragment.newInstance())
        }
        binding.bBookmark.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.search_new)
            binding.iBookmark.setImageResource(R.drawable.bookmarks_new_accent)
            binding.iProfile.setImageResource(R.drawable.profile_new)
            binding.iHome.setImageResource(R.drawable.home_new)

            startFragment(BookmarksFragment())
        }
        binding.bProfile.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.search_new)
            binding.iBookmark.setImageResource(R.drawable.bookmarks_new)
            binding.iProfile.setImageResource(R.drawable.profile_new_accent)
            binding.iHome.setImageResource(R.drawable.home_new)

            startFragment(ProfileFragment())
        }
        binding.bHome.setOnClickListener {
            binding.iSearch.setImageResource(R.drawable.search_new)
            binding.iBookmark.setImageResource(R.drawable.bookmarks_new)
            binding.iProfile.setImageResource(R.drawable.profile_new)
            binding.iHome.setImageResource(R.drawable.home_new_accent)

            startFragment(TitlesFragment.newInstance())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MenuFragment().apply {
                arguments = Bundle()
            }
    }

    private fun startFragment(newInstance: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.bodyFragment, newInstance)?.commit()
    }
}