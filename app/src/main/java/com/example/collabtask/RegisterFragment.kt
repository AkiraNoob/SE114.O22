package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.LoginFragmentBinding
import com.example.collabtask.databinding.RegisterFragmentBinding
import com.example.collabtask.model.User
import com.example.collabtask.use_case.AuthApiUseCases
import com.example.collabtask.use_case.UserApiUseCases
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.system.exitProcess

class RegisterFragment : Fragment() {
    private var _binding: RegisterFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Submit.setOnClickListener() {
            viewLifecycleOwner.lifecycleScope.launch {

                val email = binding.Email.text.toString()
                val password = binding.Password.text.toString()
                if (binding.RePassword.text.toString() != password) {
                    return@launch
                }

                AuthApiUseCases.register(email, password)
                Toast.makeText(
                    context,
                    "Đăng kí thành công, chờ trong giây lát để được chuyển hướng",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.LoginText.setOnClickListener() {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}