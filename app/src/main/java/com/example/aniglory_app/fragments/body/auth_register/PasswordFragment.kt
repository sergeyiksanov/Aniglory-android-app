package com.example.aniglory_app.fragments.body.auth_register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentPasswordBinding
import com.example.aniglory_app.fragments.body.DescriptionFragment
import com.example.aniglory_app.fragments.body.ProfileFragment
import com.example.aniglory_app.fragments.body.new_interface.NewProfileFragment
import com.example.aniglory_app.fragments.data.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PasswordFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentPasswordBinding
//    lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth

        binding.bAccept.setOnClickListener {
            if(binding.etPassword.text.length >= 8) {
                if(Data.type_login) {
                    auth.signInWithEmailAndPassword(Data.email, binding.etPassword.text.toString()).addOnCompleteListener(requireActivity()) {
                        if(it.isSuccessful) {
                            startFragment(NewProfileFragment.newInstance())
                        }
                        else {
                            binding.reload.visibility = View.VISIBLE
                            binding.exception.text = it.exception.toString()
                            binding.reload.setOnClickListener {
                                startFragment(EmailFragment.newInstance())
                            }

                            binding.viewException.visibility = View.VISIBLE
                        }
                    }
                }
                else {
                    auth.createUserWithEmailAndPassword(Data.email, binding.etPassword.text.toString()).addOnCompleteListener(requireActivity()) {
                        if(it.isSuccessful) {
                            val usernameUpd = userProfileChangeRequest {
                                displayName = Data.username
                            }

                            auth.currentUser!!.updateProfile(usernameUpd)

                            startFragment(NewProfileFragment.newInstance())
                        }
                        else {
                            binding.reload.visibility = View.VISIBLE
                            binding.exception.text = it.exception.toString()
                            binding.reload.setOnClickListener {
                                startFragment(EmailFragment.newInstance())
                            }

                            binding.viewException.visibility = View.VISIBLE
                        }
                    }


                }
            }
            else {
                binding.exception.text = "Пароль слишком короткий. Пароль должен быть миним 8 символов"
                binding.reload.visibility = View.GONE
            }
        }
    }

    private fun startFragment(instance: Fragment) {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.bodyFragment, instance)
            ?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PasswordFragment().apply {
                arguments = Bundle()
            }
    }
}