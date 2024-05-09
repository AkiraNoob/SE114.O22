package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.NavigationLayoutFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationLayoutFragment : Fragment() {
    private var _binding: NavigationLayoutFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NavigationLayoutFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, DashboardFragment()).commit()

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.DashboardMenuItem -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.child_fragment_container, DashboardFragment()).commit()
                    true
                }

                else -> {
                    true
                }
            }
        }

        binding.bottomNavigation.itemIconTintList = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}