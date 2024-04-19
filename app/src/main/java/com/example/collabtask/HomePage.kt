package com.example.collabtask

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.HomePageBinding
import com.example.collabtask.use_case.UserApiUseCases
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


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
        return binding.root
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val auth = Firebase.auth
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(
                    com.example.collabtask.R.id.action_HomePage_to_CreateProfile
                )
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

        viewLifecycleOwner.lifecycleScope.launch {
            val auth = Firebase.auth
            try {
                val user = UserApiUseCases.getUserById(auth.currentUser!!.uid.toString())
                binding.FullName.text = user!!.fullName
                if (!user.avatar.isNullOrEmpty()) {
                    Picasso.get().load(user.avatar).into(binding.Avatar)
                }
            } catch (e: Resources.NotFoundException) {
                Toast.makeText(
                    context,
                    "Có lỗi khi lấy thông tin tài khoản",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.MenuBtn.setOnClickListener() {
            val popup = PopupMenu(context, it).apply {
                setOnMenuItemClickListener(this@HomePage)
            }
            popup.menuInflater.inflate(R.menu.user_menu, popup.menu)
            popup.show()
        }

        binding.FAB.setOnClickListener() {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}