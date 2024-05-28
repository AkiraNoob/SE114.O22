package com.example.collabtask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.LoginFragmentBinding
import com.example.collabtask.model.User
import com.example.collabtask.use_case.UserApiUseCases
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    private lateinit var credentialManager: CredentialManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        credentialManager = CredentialManager.create(requireContext())
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginWGg.setOnClickListener() {
            startGoogleSignIn()
        }
    }

    private fun startGoogleSignIn() {

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.google_web_client_id))
            .setAutoSelectEnabled(false)
        .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext(),
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.w("Error", "Login error", e)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.

        when (val credential = result.credential) {

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        signInWithCredential(firebaseCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("Error", "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("Error", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("Error", "Unexpected type of credential")
            }
        }
    }

    private fun signInWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}