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
import com.example.collabtask.use_case.AuthApiUseCases
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

        binding.Submit.setOnClickListener()
        {
            findNavController().navigate(R.id.navigation_fragment)
//            viewLifecycleOwner.lifecycleScope.launch {
//                AuthApiUseCases.login(
//                    email = binding.Email.text.toString(),
//                    password = binding.Password.text.toString()
//                ).await()
//                Toast.makeText(
//                    context,
//                    "Đăng nhập thành công, chờ trong giây lát để được chuyển hướng",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}