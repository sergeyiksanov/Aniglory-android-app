package com.example.aniglory_app.fragments.body.auth_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentEmailBinding
import com.example.aniglory_app.fragments.body.ProfileFragment
import com.example.aniglory_app.fragments.data.Data

class EmailFragment : Fragment() {

    lateinit var binding: FragmentEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEmailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bAccept.setOnClickListener {
            if(binding.etEmail.text != null) {
                Data.email = binding.etEmail.text.toString()

                if(Data.type_login) {
                    startFragment(PasswordFragment.newInstance())
                }
                else {
                    startFragment(UsernameFragment.newInstance())
                }
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
            EmailFragment().apply {
                arguments = Bundle()
            }
    }
}