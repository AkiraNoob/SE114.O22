package com.example.collabtask.use_case

import com.example.collabtask.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthApiUseCases {

    companion object {
        private val auth = Firebase.auth

        suspend fun register(email: String, password: String, fullName: String) {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(userId = authResult.user!!.uid.toString(), fullName = fullName, email = email)
            UserApiUseCases.createUser(user)
        }

        fun login(email: String, password: String): Task<AuthResult> {
             return auth.signInWithEmailAndPassword(email, password)
        }
    }
}