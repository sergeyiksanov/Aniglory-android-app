package com.example.aniglory_app.fragments.body

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.clear
import coil.dispose
import coil.load
import com.example.aniglory_app.R
import com.example.aniglory_app.databinding.FragmentProfileBinding
import com.example.aniglory_app.fragments.body.auth_register.EmailFragment
import com.example.aniglory_app.fragments.data.Data
import com.example.aniglory_app.values.Network
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    lateinit var pickedPhoto: Uri
    lateinit var pickedBitMap: Bitmap

    lateinit var binding: FragmentProfileBinding
    lateinit var auth: FirebaseAuth
    lateinit var dialog: Dialog
    val db = Firebase.firestore
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun pickPhoto(view: View) {
        if(ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        else {
            val galeriInent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriInent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriInent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriInent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data!!

            editData(0, pickedPhoto, "")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.findViewById<FrameLayout>(R.id.headerFragment)!!.visibility = View.GONE
        activity?.findViewById<FrameLayout>(R.id.footerFragment)!!.visibility = View.VISIBLE

        binding.about.setOnClickListener { Data.fun_is_development(requireActivity()) }
        binding.help.setOnClickListener { showCustomDialog(2) }
        binding.promo.setOnClickListener { Data.fun_is_development(requireActivity()) }

        auth = Firebase.auth

//        if(checkAuthState()) {
//            if(auth.currentUser!!.isEmailVerified) {
//                binding.viewWarning.visibility = View.GONE
//            }
//            else {
//                auth.currentUser!!.sendEmailVerification()
//                binding.viewWarning.visibility = View.VISIBLE
//            }
//        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            }
            catch (e: ApiException) {

            }
        }

        dialog = Dialog(requireActivity())

        updateData()

    }

    private fun updateData() {
        if(checkAuthState()) {
            binding.avatar.load(auth.currentUser!!.photoUrl)
            binding.avatarBackground.load(auth.currentUser!!.photoUrl)
            binding.avatar.visibility = View.VISIBLE
            binding.username.text = auth.currentUser!!.displayName

            binding.account.text = "Выйти из аккаунта"
            binding.account.setOnClickListener { auth.signOut().apply { updateData() } }

            binding.avatar.setOnClickListener { pickPhoto(requireView()) }
            binding.username.setOnClickListener { showCustomDialog(1) }


//            if(auth.currentUser!!.isEmailVerified) {
//                binding.viewWarning.visibility = View.GONE
//            }
//            else {
//                auth.currentUser!!.sendEmailVerification()
//                binding.viewWarning.visibility = View.VISIBLE
//            }
        }
        else {
            binding.avatar.load(R.drawable.profile_new)
            binding.avatarBackground.load(R.drawable.profile_new)
            binding.username.text = "Вы не вошли в аккаунт"
            binding.account.text = "Войти в аккаунт"

            binding.account.setOnClickListener { showCustomDialog(0) }
        }
    }

    override fun onPause() {
        super.onPause()

        updateData()
    }

    private fun editData(type : Int, resourseUri : Uri?, resourseString : String) {
        val profileUpdates = userProfileChangeRequest {
            when(type) {
                0 -> {
                    photoUri = resourseUri
                }
                1 -> {
                    displayName = resourseString
                }
            }
        }

        auth.currentUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateData()
                }
            }
    }

    private fun showCustomDialog(type : Int) {

        when(type) {
            0 -> {
                dialog.setContentView(R.layout.login_selection)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val bLogin: Button = dialog.findViewById(R.id.bLogin)
                val bRegister: Button = dialog.findViewById(R.id.bRegister)
                val tOtherMethod: TextView = dialog.findViewById(R.id.textView15)
                val bGoogle: ImageView = dialog.findViewById(R.id.bGoogle)

                bGoogle.setOnClickListener { signInWithGoogle() }
                bLogin.setOnClickListener {
                    startFragment(true)
                    dialog.dismiss()
                }
                bRegister.setOnClickListener {
                    startFragment(false)
                    dialog.dismiss()
                }
            }
            1 -> {
                dialog.setContentView(R.layout.edit_user_name)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val edUn: EditText = dialog.findViewById(R.id.editUserName)
                val accept: Button = dialog.findViewById(R.id.accept)

                accept.setOnClickListener {
                    if(edUn.text != null) {
                        Data.editUserResourse = edUn.text.toString()
                        editData(1, null, Data.editUserResourse)
                        dialog.dismiss()
                    }
                }
            }
            2 -> {
                dialog.setContentView(R.layout.helpers_dialog)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val tg: Button = dialog.findViewById(R.id.tg)

                tg.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Network.TELEGRAMM_HELP))
                    startActivity(intent)
                    dialog.dismiss()
                }
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show();
    }

    private fun startFragment(type: Boolean) {
        Data.type_login = type

        fragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.bodyFragment, EmailFragment.newInstance())
            ?.commit()
    }

    private fun checkAuthState(): Boolean {
        return auth.currentUser != null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle()
            }
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            updateData()
            dialog.dismiss()
        }
    }
}