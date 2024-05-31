package com.example.collabtask.use_case

import android.content.res.Resources.NotFoundException
import com.example.collabtask.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class UserApiUseCases {
    companion object {
        private var userFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("user")

        suspend fun createUser(user: User) {
            userFirestore.document(user.userId).set(user).await()
        }

        suspend fun getUserById(userId: String): User? {
            val user = userFirestore.document(userId).get().await()

            if (!user.exists()) {
               return null
            }
            return user.toObject(User::class.java)
        }

        suspend fun updateUser(userId: String, user: User) {
            userFirestore.document(userId).set(user, SetOptions.merge()).await()
        }
    }
}