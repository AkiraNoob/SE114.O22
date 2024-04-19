package com.example.collabtask

import android.content.res.Resources.NotFoundException
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.CreateProfileBinding
import com.example.collabtask.databinding.LoginFragmentBinding
import com.example.collabtask.databinding.RegisterFragmentBinding
import com.example.collabtask.model.User
import com.example.collabtask.use_case.AuthApiUseCases
import com.example.collabtask.use_case.UserApiUseCases
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.system.exitProcess

class CreateProfile : Fragment() {
    private var _binding: CreateProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        storage = Firebase.storage

        val storageRef = storage.reference
        val fireStore = Firebase.storage.reference

        val auth = Firebase.auth

        viewLifecycleOwner.lifecycleScope.launch {
            try {

                val user = UserApiUseCases.getUserById(auth.currentUser!!.uid.toString())
                binding.FullName.setText(user!!.fullName)
                binding.Bio.setText(user.bio)
                if (!user.avatar.isNullOrEmpty()) {
                    Picasso.get().load(user.avatar).into(binding.AvatarDisplay)
                }


            } catch (e: NotFoundException) {
                Toast.makeText(
                    context,
                    "Có lỗi khi lấy thông tin tài khoản",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.Submit.setOnClickListener {
            auth.signOut()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Picasso.get().load(uri).into(binding.AvatarDisplay)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        binding.Avatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.Submit.setOnClickListener {
            val fullName = binding.FullName.text.toString()
            val bio = binding.Bio.text.toString()

            viewLifecycleOwner.lifecycleScope.launch {
                UserApiUseCases.updateUser(
                    auth.currentUser!!.uid.toString(),
                    User(fullName = fullName, bio = bio)
                )
            }
        }

        binding.GoBack.setOnClickListener {
            findNavController().navigateUp()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}