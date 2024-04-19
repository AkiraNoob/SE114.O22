package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.HomePageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomePage : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: HomePageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomePageBinding.inflate(inflater, container, false)

        binding.MenuBtn.setOnClickListener() {
            val popup = PopupMenu(context, it).apply {
                setOnMenuItemClickListener(this@HomePage)
            }
            popup.menuInflater.inflate(R.menu.user_menu, popup.menu)
            popup.show()
        }

        binding.FAB.setOnClickListener() {
        }

        return binding.root

    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val auth = Firebase.auth
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(
                    com.example.collabtask.R.id.action_HomePage_to_CreateProfile)
                true
            }
            R.id.action_logout -> {
                auth.signOut()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}