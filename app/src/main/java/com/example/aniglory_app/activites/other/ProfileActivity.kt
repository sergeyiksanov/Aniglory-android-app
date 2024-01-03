package com.example.aniglory_app.activites.other

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var auth: FirebaseAuth
    lateinit var dialog: Dialog
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)

        auth = Firebase.auth

        binding.button.setOnClickListener { startRegisterActivity() }
        binding.imageView2.setOnClickListener { showCustomDialog() }

        updateData()

        if(checkAuthState()) {
//            updOtherData()
            getDocument()
        }
    }

    private fun checkAuthState(): Boolean {
        return auth.currentUser != null
    }

    private fun updateData() {
        if(checkAuthState()) {
            binding.imageView.load(auth.currentUser!!.photoUrl)
            binding.textView10.text = auth.currentUser!!.displayName

            binding.imageView.visibility = View.VISIBLE
            binding.textView10.visibility = View.VISIBLE
            binding.imageView2.visibility = View.VISIBLE
            binding.button.visibility = View.INVISIBLE
        }
        else {
            binding.imageView.visibility = View.INVISIBLE
            binding.textView10.visibility = View.INVISIBLE
            binding.imageView2.visibility = View.INVISIBLE
            binding.button.visibility = View.VISIBLE
        }
    }

    private fun startRegisterActivity() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun editUsName(newUsName: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newUsName
        }

        auth.currentUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener {
                updateData()
            }

        dialog.dismiss()
    }

    private fun showCustomDialog() {
        dialog.setContentView(R.layout.edit_user_name)
        val accept: Button = dialog.findViewById(R.id.accept)
        val edit: TextView = dialog.findViewById(R.id.editUserName)

        accept.setOnClickListener { editUsName(edit.text.toString()) }

        dialog.show();
    }

//    private fun updOtherData() {
//        val data = hashMapOf(
//            "viewing time" to 0
//        )
//
//        db.collection("users").document(auth.currentUser!!.uid).set(data)
//    }

    private fun getDocument() {
        val docRef = db.collection("users").document(auth.currentUser!!.uid)

        docRef.get()
            .addOnCompleteListener { document ->
                if(document != null) {
//                    if(document.result.data!!["viewing time: day"] != 0) {
//                        if(document.result.data!!["viewing time: hour"] != 0) {
//                            binding.textView11.text = document.result.data!!["viewing time: day"].toString() + " дней," + document.result.data!!["viewing time: hour"].toString() + " часов"
//                        }
//                        else {
//                            binding.textView11.text = document.result.data!!["viewing time: day"].toString() + " дней"
//                        }
//                    }
//                    else if(document.result.data!!["viewing time: hour"] != 0) {
//                        if(document.result.data!!["viewing time: minute"] != 0) {
//                            binding.textView11.text = document.result.data!!["viewing time: hour"].toString() + " часов," + document.result.data!!["viewing time: minute"].toString() + " минут"
//                        }
//                        else {
//                            binding.textView11.text = document.result.data!!["viewing time: hour"].toString() + " часов"
//                        }
//                    }
                    if(document.result.data!!["viewing time: minute"] != 0) {
                        if(document.result.data!!["viewing time: second"] != 0) {
                            binding.textView11.text = document.result.data!!["viewing time: minute"].toString() + " минут," + document.result.data!!["viewing time: second"].toString() + " секунд"
                        }
                        else {
                            binding.textView11.text = document.result.data!!["viewing time: minute"].toString() + " минут"
                        }
                    }
                }
                else {
                    binding.textView11.text = "0"
                }
            }
    }
}