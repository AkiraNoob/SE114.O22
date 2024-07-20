package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.collabtask.databinding.ActivityMainBinding
import com.example.collabtask.model.User
import com.example.collabtask.use_case.UserApiUseCases
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_container)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        authStateListener = AuthStateListener {
            if (auth.currentUser != null) {
                lifecycleScope.launch {
                    if (UserApiUseCases.getUserById(auth.currentUser!!.uid) == null) {
                        val user = User(
                            auth.currentUser!!.uid,
                            fullName = auth.currentUser!!.email.toString(),
                            email = auth.currentUser!!.email.toString()
                        )
                        UserApiUseCases.createUser(user)
                    }
                    val intent = Intent(applicationContext, DashboardFragment::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        auth.removeAuthStateListener(authStateListener)
        super.onDestroy()
    }
}