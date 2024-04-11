package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.LoginFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = Firebase.auth

        binding.Submit.setOnClickListener()
        {
            auth.signInWithEmailAndPassword(binding.Email.text.toString(), binding.Password.text.toString()).addOnCompleteListener()
            {
                Toast.makeText(context, "Đăng nhập thành công, chờ trong giây lát để được chuyển hướng", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.HomePage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}