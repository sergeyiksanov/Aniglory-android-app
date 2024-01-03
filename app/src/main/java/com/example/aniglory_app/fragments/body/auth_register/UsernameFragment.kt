package com.example.aniglory_app.fragments.body.auth_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentUsernameBinding
import com.example.aniglory_app.fragments.data.Data

class UsernameFragment : Fragment() {
    lateinit var binding: FragmentUsernameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsernameBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.accept.setOnClickListener {
            if(binding.username.text != null) {
                Data.username = binding.username.text.toString()

                startFragment(PasswordFragment.newInstance())

            }
        }
    }

    private fun startFragment(instance: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, instance)
            ?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UsernameFragment().apply {
                arguments = Bundle()
            }
    }
}