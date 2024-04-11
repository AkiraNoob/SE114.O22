package com.example.collabtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.collabtask.databinding.HomePageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomePage : Fragment() {

    private var _binding: HomePageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomePageBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = Firebase.auth

        binding.logout.setOnClickListener()
        {
            auth.signOut()
            findNavController().navigate(R.id.LoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}